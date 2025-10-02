import re
from playwright.sync_api import sync_playwright, expect
import os

def run_verification(playwright):
    # Get absolute path to index.html
    html_file_path = "file://" + os.path.abspath("index.html")

    browser = playwright.chromium.launch(headless=True)
    page = browser.new_page()

    # 1. Go to the game page
    page.goto(html_file_path)

    # It might start on a 'new game' screen if no save exists.
    # We'll handle this by clicking the standard game button if it appears.
    new_game_button = page.locator("#new-game-standard")
    if new_game_button.is_visible():
        new_game_button.click()
        page.locator("#final-settings-start-game-btn").click()

    # Wait for game to be ready by checking for the initial cash display
    expect(page.locator("#cash-display")).to_have_text("100")

    # 2. Change the game state by buying an item
    page.evaluate("() => { openClipboardPanel(); }")
    page.locator("#app-btn-order").click()

    # Wait for the order panel to be populated. The "Drawing" category is collapsed
    # by default, so we need to click its header to expand it first.
    drawing_header = page.locator("h3:has-text('Drawing')")
    expect(drawing_header).to_be_visible()
    drawing_header.click()

    # Now we can buy a Pencil
    buy_button = page.locator("#buy-now-one-Pencil")
    expect(buy_button).to_be_visible()
    buy_button.click()

    # Assert that cash has decreased after the purchase
    expect(page.locator("#cash-display")).to_have_text("99")

    # 3. Start a NEW game
    # Phone should still be open. We are in the order panel, so go back to the app grid.
    page.locator("#phone-back-btn").click()

    # Now navigate to settings from the app grid
    page.locator("#app-btn-settings").click()
    page.locator("#new-game-btn").click()

    # The clipboard panel can obscure the modal, so we manually close it.
    page.evaluate("() => { document.getElementById('clipboard-panel').classList.remove('open'); }")

    page.locator("#new-game-standard").click()
    page.locator("#final-settings-start-game-btn").click()

    # 4. Assert that the game state has been reset
    # There might be a race condition where the UI updates out of order.
    # We'll force a UI update right before the assertion to ensure it reflects the correct state.
    page.evaluate("() => { window.updateUI(); }")

    expect(page.locator("#day-display")).to_have_text("1")
    expect(page.locator("#cash-display")).to_have_text("100")

    # 5. Take a screenshot for visual confirmation
    page.screenshot(path="jules-scratch/verification/verification.png")

    browser.close()

with sync_playwright() as playwright:
    run_verification(playwright)

print("Verification script finished and screenshot taken.")