import asyncio
import os
from playwright.async_api import async_playwright, expect

async def main():
    # Setup
    port = os.environ.get("PORT", "8000")
    host_url = f"http://localhost:{port}/index.html"

    async with async_playwright() as p:
        # Launch browser
        browser = await p.chromium.launch(headless=True)
        context = await browser.new_context()

        # Host page
        host_page = await context.new_page()
        await host_page.goto(host_url)

        # DEBUG: Save host HTML to diagnose visibility issue
        await host_page.wait_for_timeout(1000) # Give scripts time to run
        host_html = await host_page.content()
        with open("jules-scratch/host_page.html", "w") as f:
            f.write(host_html)
        print("📝 Host HTML saved to jules-scratch/host_page.html")

        # This is the line that is expected to fail.
        # The HTML snapshot from the previous step will help debug why.
        await host_page.click('#new-game-btn')
        print("✅ Clicked 'Start New Game' on host.")

        # The rest of the script won't be reached, but is kept for context
        await host_page.click('#final-settings-new-game-btn')
        await expect(host_page.locator('#day-counter')).to_be_visible()
        print("✅ Default game started on host.")

        # Companion setup...
        companion_url = f"{host_url}?companion=true"
        await host_page.click('#companion-app-btn')
        host_code_element = host_page.locator('#host-code')
        await expect(host_code_element).to_be_visible()
        host_code = await host_code_element.input_value()

        companion_page = await context.new_page()
        await companion_page.goto(companion_url)
        await companion_page.fill('#companion-code-input', host_code)
        await companion_page.click('#connect-btn')
        await expect(companion_page.locator('#phone-ui')).to_be_visible(timeout=10000)

        await host_page.click('#close-companion-code-btn')
        market_app_host = host_page.locator('#app-icon-market')
        await market_app_host.click()

        market_panel_companion = companion_page.locator('#market-panel')
        await expect(market_panel_companion).to_be_visible(timeout=5000)

        item_row = market_panel_companion.locator('.grid-cols-4').first
        await expect(item_row).to_be_visible()

        await browser.close()

if __name__ == "__main__":
    asyncio.run(main())