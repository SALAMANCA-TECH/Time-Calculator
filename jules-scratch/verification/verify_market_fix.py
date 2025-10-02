import asyncio
from playwright.async_api import async_playwright, expect
import os
import re

async def main():
    async with async_playwright() as p:
        browser = await p.chromium.launch()
        context = await browser.new_context()
        host_page = await context.new_page()
        companion_page = await context.new_page()

        # 1. Go to the host page
        file_path = os.path.abspath('index.html')
        host_url = f'file://{file_path}'
        await host_page.goto(host_url)

        # 2. Start a default game directly on the host to bypass the new game modals
        await host_page.evaluate("() => startGame({})")
        print("✅ Default game started on host.")

        # 3. Connect the companion
        await host_page.click('#companion-sync-btn')
        open_in_tab_btn = await host_page.wait_for_selector('#open-in-tab-btn:not(.hidden)')
        companion_url = await open_in_tab_btn.get_attribute('href')
        await companion_page.goto(companion_url)

        # Wait for the handshake to complete
        await expect(host_page.locator('#host-status')).to_have_text('✅ Connected!', timeout=10000)
        await expect(companion_page.locator('#companion-status')).to_have_text('✅ Connected!', timeout=10000)
        print("✅ Host and companion connected.")

        # 4. Open the Market Panel from the host
        await host_page.evaluate("() => openClipboardPanel()")
        await host_page.click('#app-btn-market')
        print("Host clicked 'Market' app.")

        # 5. Verify the Market Panel is visible and populated on the companion
        market_panel_companion = companion_page.locator('#market-panel')
        await expect(market_panel_companion).to_be_visible(timeout=5000)

        # Check that it's populated by looking for the first category button
        first_category_button = companion_page.locator('button[id^="market-category-"]').first
        await expect(first_category_button).to_be_visible(timeout=5000)
        print("✅ Market panel is visible and populated on companion.")

        # 6. Navigate to the Detail Panel on the companion
        await first_category_button.click()
        market_detail_panel_companion = companion_page.locator('#market-detail-panel')
        await expect(market_detail_panel_companion).to_be_visible(timeout=5000)
        await expect(market_panel_companion).to_be_hidden()

        # 7. Assert that the detail panel is populated with item buttons
        first_item_button = companion_page.locator('button[id^="market-item-"]').first
        await expect(first_item_button).to_be_visible(timeout=5000)
        print("✅ Market detail panel is visible and populated on companion.")

        # 8. Take a screenshot for visual confirmation
        screenshot_path = 'jules-scratch/verification/verification.png'
        await companion_page.screenshot(path=screenshot_path)
        print(f"📸 Screenshot saved to {screenshot_path}")

        await browser.close()

if __name__ == '__main__':
    asyncio.run(main())