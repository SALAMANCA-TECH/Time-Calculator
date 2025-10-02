import asyncio
import re
from playwright.async_api import async_playwright, expect
import os

async def main():
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)

        # Get the absolute path for the index.html file
        # This is robust to wherever the script is run from.
        index_path = "file://" + os.path.abspath("index.html")

        # --- Host Setup ---
        host_page = await browser.new_page()
        await host_page.goto(index_path)

        # Wait for the host to be ready and get its PeerJS ID
        host_id = await host_page.wait_for_function("() => window.peer && window.peer.id")
        host_peer_id = await host_id.json_value()
        print(f"Host PeerJS ID: {host_peer_id}")

        # --- Companion Setup ---
        companion_page = await browser.new_page()
        companion_url = f"{index_path}?mode=companion&companion_id={host_peer_id}"
        await companion_page.goto(companion_url)

        # Wait for the companion to confirm connection
        await expect(companion_page.locator("#companion-status")).to_have_text("✅ Connected!")
        print("Companion connected to host.")

        # --- Start Game on Host ---
        # The host will receive a 'companion-ready' signal and might show a modal.
        # We need to click the button on the *companion* to start the game.
        await companion_page.locator("#new-game-standard").click()
        await asyncio.sleep(1) # Give a moment for the next screen to appear
        await companion_page.locator("#final-settings-start-game-btn").click()
        print("New game started via companion.")

        # Wait for the game to be fully loaded on the companion
        await expect(companion_page.locator("#phone-apps-grid")).to_be_visible()
        print("Companion UI is ready.")

        # --- Verification Steps on Companion ---

        # 1. Open the Market app
        await companion_page.locator("#app-btn-market").click()
        await expect(companion_page.locator("#market-panel")).to_be_visible()
        print("Market panel opened on companion.")

        # 2. Click on the "Drawing" category to open the detail panel
        # The button ID is dynamically generated, so we target it carefully.
        await companion_page.locator("#market-category-0").click()
        await expect(companion_page.locator("#market-detail-panel")).to_be_visible()
        print("Market detail panel for 'Drawing' opened.")

        # 3. Verify the panel is populated
        # We'll check if the panel contains a button for one of the items in that category, e.g., "Pencil".
        # This proves the `storageCells` data was correctly received and rendered.
        item_button_locator = companion_page.locator("#market-item-Drawing-0") # This corresponds to the first item in the Drawing category

        await expect(item_button_locator).to_be_visible(timeout=10000)
        await expect(item_button_locator).to_contain_text("Pencil")
        print("Verification successful: Market detail panel is populated with item data.")

        # Take a screenshot for final confirmation
        screenshot_path = "jules-scratch/companion_market_modal_populated.png"
        await companion_page.screenshot(path=screenshot_path)
        print(f"Screenshot saved to {screenshot_path}")

        await browser.close()

if __name__ == "__main__":
    asyncio.run(main())