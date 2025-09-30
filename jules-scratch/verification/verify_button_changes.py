import asyncio
import os
from playwright.async_api import async_playwright, expect

async def main():
    async with async_playwright() as p:
        browser = await p.chromium.launch()
        page = await browser.new_page()

        # Navigate to the local index.html file
        await page.goto(f"file://{os.path.abspath('index.html')}")

        # Open the phone/clipboard by directly calling the JavaScript function
        await page.evaluate("openClipboardPanel()")

        # --- Verify Market Panel ---
        await page.click("#app-btn-market")

        # Check that the first category is a button and click it
        market_category_button = page.locator("button#market-category-0")
        await expect(market_category_button).to_be_visible()
        await market_category_button.click()

        # Check that the first item in the detail view is a button
        market_item_button = page.locator("button#market-item-Drawing-0")
        await expect(market_item_button).to_be_visible()

        # --- Go back and Verify Customers Panel ---
        await page.click("#market-detail-back-btn")
        await page.click("#phone-back-btn")

        # Force a customer to spawn so the list isn't empty
        await page.evaluate("spawnNewCustomer()")

        await page.click("#app-btn-customers")

        # Check that the first customer item is a button
        customer_list = page.locator("#in-store-customer-list")
        first_customer_button = customer_list.locator("button").first
        await expect(first_customer_button).to_be_visible()

        # Take a screenshot of the final state (customers panel)
        await page.screenshot(path="jules-scratch/verification/verification.png")

        await browser.close()

if __name__ == "__main__":
    asyncio.run(main())