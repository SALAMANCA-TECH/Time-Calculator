from playwright.sync_api import sync_playwright, expect
import os

def run_verification():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        page = browser.new_page()

        # Get the absolute path to the index.html file
        current_working_directory = os.getcwd()
        file_path = f"file://{os.path.join(current_working_directory, 'index.html')}"

        # Go to the local file
        page.goto(file_path)

        # The game starts on the "New Game" screen. Click the standard game button.
        page.locator("#new-game-standard").click()

        # Now on the "Final Settings" screen. Click the start game button.
        page.locator("#final-settings-start-game-btn").click()

        # The game should now be running. Wait for the main UI to be visible.
        expect(page.locator("#game-ui")).to_be_visible()

        # Open the clipboard/phone by evaluating the game's JavaScript function.
        page.evaluate("openClipboardPanel()")

        # Wait for the phone panel to be open and visible.
        expect(page.locator("#clipboard-panel.open")).to_be_visible()

        # Click the "Unlocks" app button.
        page.locator("#app-btn-unlocks").click()

        # Verify the unlocks panel is now visible inside the phone UI.
        expect(page.locator("#unlocks-panel")).to_be_visible()

        # Take a screenshot to visually confirm the result.
        page.screenshot(path="jules-scratch/verification/verification.png")

        browser.close()

if __name__ == "__main__":
    run_verification()