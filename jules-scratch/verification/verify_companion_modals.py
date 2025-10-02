import asyncio
import re
from playwright.async_api import async_playwright, expect
import os

async def main():
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)

        # Get the absolute path for the index.html file
        index_path = "file://" + os.path.abspath("index.html")

        # --- Host Setup ---
        host_context = await browser.new_context()
        host_page = await host_context.new_page()
        await host_page.goto(index_path)

        # The "New Game" modal should be visible on the host initially
        await expect(host_page.locator("#new-game-screen")).to_be_visible()
        print("Host: Initial 'New Game' modal is visible.")

        # Click the companion sync button to open the QR code modal
        # Force the click because the "New Game" modal may be obscuring it.
        await host_page.locator("#companion-sync-btn").click(force=True)
        await expect(host_page.locator("#host-sync-modal")).to_be_visible()
        print("Host: QR code modal is visible.")

        # Wait for the host to be ready and get its PeerJS ID
        host_id_handle = await host_page.wait_for_function("() => window.peer && window.peer.id", timeout=15000)
        host_peer_id = await host_id_handle.json_value()
        print(f"Host: PeerJS ID is '{host_peer_id}'")

        # --- Companion Setup ---
        companion_context = await browser.new_context()
        companion_page = await companion_context.new_page()
        companion_url = f"{index_path}?mode=companion&companion_id={host_peer_id}"
        await companion_page.goto(companion_url)

        # --- Verification: Modal Transfer ---
        # On the companion, the "New Game" modal should now be visible.
        await expect(companion_page.locator("#new-game-screen")).to_be_visible(timeout=15000)
        print("Companion: 'New Game' modal is now visible.")

        # On the host, the "New Game" modal should have been hidden.
        await expect(host_page.locator("#new-game-screen")).to_be_hidden()
        print("Host: 'New Game' modal is now hidden.")

        # --- Verification: Game Start from Companion ---
        # 1. Click "Standard" on the companion
        await companion_page.locator("#new-game-standard").click()
        await expect(companion_page.locator("#final-settings-screen")).to_be_visible()
        print("Companion: Clicked 'Standard', final settings screen is visible.")

        # 2. Click "Start Game" on the companion
        await companion_page.locator("#final-settings-start-game-btn").click()

        # 3. The game should start. On the companion, this means the app grid is visible.
        await expect(companion_page.locator("#phone-apps-grid")).to_be_visible()
        print("Companion: Game started, app grid is visible.")

        # 4. On the host, the game UI should be active. We'll check the day display.
        await expect(host_page.locator("#day-display")).to_have_text("1")
        print("Host: Game started, UI is active.")

        # --- Final Screenshot ---
        screenshot_path = "jules-scratch/verification/companion_game_started.png"
        await companion_page.screenshot(path=screenshot_path)
        print(f"Verification successful. Screenshot saved to {screenshot_path}")

        await browser.close()

if __name__ == "__main__":
    asyncio.run(main())