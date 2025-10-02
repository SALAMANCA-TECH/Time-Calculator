import os
import re
from playwright.sync_api import sync_playwright, expect

def run_verification(playwright):
    browser = playwright.chromium.launch(headless=True)
    context = browser.new_context()
    page = context.new_page()

    # Navigate to the local index.html file
    page.goto(f"file://{os.path.abspath('index.html')}")

    # --- Reduce game day duration for faster testing ---
    # This makes the entire day pass in a fraction of a second.
    page.evaluate("() => { OPENING_DURATION = 10; DAY_SHIFT_DURATION = 10; NIGHT_SHIFT_DURATION = 10; CLOSING_DURATION = 10; BREAK_DURATION = 5; }")

    # --- Start a "Family Store" game ---
    page.wait_for_selector("#new-game-family", state="visible")
    page.click("#new-game-family")

    # On the family store options screen, select the "Stocker" as the relative
    page.wait_for_selector("#family-store-screen", state="visible")
    page.select_option("#family-relative-select", "stocker")

    # Click the start game button on the family store screen
    page.click("#family-start-game-btn")

    # On the final settings screen, just click the final start button
    page.wait_for_selector("#final-settings-screen", state="visible")
    page.click("#final-settings-start-game-btn")

    # --- Wait for the day to end ---
    # The startGame() function automatically starts the day. With the shortened
    # durations, the day will end very quickly. We just wait for the report.
    end_of_day_panel = page.locator("#end-of-day-panel")
    expect(end_of_day_panel).to_be_visible(timeout=10000)

    # --- Verify the report ---
    # Click the "Bills" tab in the report
    page.click("#report-toggle-bills")

    # Find the specific line for the Stocker's salary
    stocker_salary_line = page.locator("p", has_text=re.compile(r"Stocker's Salary"))
    expect(stocker_salary_line).to_be_visible()

    # The salary is in the last span within that paragraph
    stocker_salary_span = stocker_salary_line.locator("span").last

    # Assert that the salary is exactly "-$0.00"
    expect(stocker_salary_span).to_have_text("-$0.00")

    # Take a screenshot of the bills report for visual confirmation
    page.screenshot(path="jules-scratch/verification/verification.png")

    print("Verification successful: Stocker's salary is $0.00 in the report.")

    browser.close()

with sync_playwright() as playwright:
    run_verification(playwright)