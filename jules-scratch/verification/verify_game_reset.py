import asyncio
from playwright.async_api import async_playwright, expect
import re
import os

async def main():
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        page = await browser.new_page()

        # Navigate to the local game file
        await page.goto(f"file:///{os.getcwd()}/index.html")

        # --- First Game ---
        # Start a standard game
        await page.locator("#new-game-standard").click()
        # Click through the final settings screen
        await page.locator("#final-settings-start-game-btn").click()

        # Wait for the game to start and the 'Start Day' button to be hidden
        await expect(page.locator("#start-day-btn")).to_be_hidden(timeout=10000)

        # Let the day run for a moment, then end it to create some state
        await page.evaluate("() => { nextDay(); }")

        # --- Second Game (The Test) ---
        # From the end-of-day report, open the phone and start a new game
        await page.locator("#next-day-report-btn").click()
        await page.evaluate("() => { openClipboardPanel(); }")
        await page.locator("#app-btn-settings").click()
        await page.locator("#new-game-btn").click()
        await page.evaluate("() => { closeClipboard(); }") # Close the phone to reveal the modal

        # Start another standard game
        await page.locator("#new-game-standard").click()
        await page.locator("#final-settings-start-game-btn").click()

        # --- Verification ---
        # The main verification is that the game is now active.
        # A good proxy for this is that the 'Start Day' button, which is initially
        # visible after the end-of-day report, is now hidden because startGame()
        # was called and the game loop is running.
        await expect(page.locator("#start-day-btn")).to_be_hidden(timeout=10000)

        # Also, check that the day is 1
        await expect(page.locator("#day-display")).to_have_text("1")

        # Check that cash is 100
        await expect(page.locator("#cash-display")).to_have_text("100")

        # Take a screenshot to confirm the UI looks correct
        await page.screenshot(path="jules-scratch/verification/verify_reset_fix.png")

        print("Verification script completed successfully.")

        await browser.close()

if __name__ == "__main__":
    import os
    asyncio.run(main())