import asyncio
from playwright.async_api import async_playwright, expect
import os
import re

async def main():
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        page = await browser.new_page()

        # Navigate to the local index.html file
        file_path = os.path.abspath('index.html')
        await page.goto(f'file://{file_path}')

        # Get locators
        companion_button = page.locator('#companion-sync-btn')
        clipboard_panel = page.locator('#clipboard-panel')
        clipboard_paper = page.locator('#clipboard-paper')

        # --- Step 1: Initial State Verification ---
        await expect(companion_button).not_to_have_class(re.compile(r'phone-connected'))
        await expect(clipboard_panel).not_to_have_class(re.compile(r'open'))

        # --- Step 2: Simulate Companion Connection ---
        # This JS block mimics the `listenForCompanionConnections` function's relevant parts
        await page.evaluate("""() => {
            document.getElementById('companion-sync-btn').classList.add('phone-connected');
            openClipboardPanel();
            document.getElementById('clipboard-paper').classList.add('hidden');
        }""")

        # --- Step 3: Verify Connected State ---
        # Wait for animations
        await page.wait_for_timeout(500)

        # Check that the panel is open, but the paper inside is hidden
        await expect(clipboard_panel).to_have_class(re.compile(r'open'))
        await expect(clipboard_paper).to_be_hidden()

        # Check that the button is green
        await expect(companion_button).to_have_class(re.compile(r'phone-connected'))

        # Take screenshot for visual verification
        await page.screenshot(path='jules-scratch/verification/verification_connected.png')

        # --- Step 4: Simulate Companion Disconnection ---
        # This JS block mimics the `hostConnection.on('close', ...)` function
        await page.evaluate("""() => {
            document.getElementById('companion-sync-btn').classList.remove('phone-connected');
            document.getElementById('clipboard-paper').classList.remove('hidden');
            closeClipboard();
        }""")

        # --- Step 5: Verify Disconnected State ---
        # Wait for animations
        await page.wait_for_timeout(500)

        await expect(companion_button).not_to_have_class(re.compile(r'phone-connected'))
        await expect(clipboard_panel).not_to_have_class(re.compile(r'open'))
        await expect(clipboard_paper).to_be_visible()

        await browser.close()

asyncio.run(main())