import re
from playwright.sync_api import sync_playwright, expect
import os

def run_verification(playwright):
    # Get the absolute path to index.html
    # This is necessary because the test is run from a different working directory.
    html_file_path = "file://" + os.path.abspath("index.html")

    browser = playwright.chromium.launch(headless=True)
    page = browser.new_page()

    # Go to the local index.html file
    page.goto(html_file_path)

    # 1. SETUP: Start a new game with specific conditions
    # We will use page.evaluate to set up the game state directly.
    # This makes the test deterministic and avoids complex UI interactions for setup.
    page.evaluate("""() => {
        // Choose a market event that makes 'Drawing' items popular
        const sketchDailyHeadline = {
            headline: "'Sketch Daily' App Goes Viral",
            description: "A new mobile app encouraging daily drawing habits has exploded in popularity, causing a boom in demand for basic supplies. Suppliers are offering discounts to capture the new wave of artists.",
            effects: [{ category: "Drawing", modifier: -0.10, items: "all" }]
        };

        // Set up options for a new game
        const options = {
            marketType: 'dailyMood',
            continuousMode: true,
            dev: {
                freeUnlocks: false,
                freeSupplies: false,
                startWithDebt: false
            }
        };

        // Manually set unlocks to ensure 'Drawing' items are available
        unlocks.storage[0] = true; // Unlock 'Drawing' storage

        // Start the game with these specific options
        startGame(options);

        // Manually set game state for Day 2 to ensure market effects are active
        day = 2;
        marketForecast = generateMarketForecast(); // Generate a forecast

        // Force the specific market event for today
        const forecastIndex = (day - 2 + 7) % 7;
        marketForecast[forecastIndex].recommendations = ['Pencil', 'Charcoal', 'Markers', 'Sketchbook'];

        // Manually spawn several customers to check their requests
        for (let i = 0; i < 5; i++) {
            spawnNewCustomer();
        }

        // Ensure UI reflects the new day
        updateUI();
    }""")

    # 2. ACTION: Open the phone and go to the customers panel
    page.evaluate("openClipboardPanel()")
    page.get_by_role("button", name="Customers").click()

    # 3. ASSERTION & SCREENSHOT
    # We expect to see customers in the list. Their requests should be biased
    # towards the "in-demand" drawing items.
    in_store_list = page.locator("#in-store-customer-list")
    expect(in_store_list.get_by_role("button")).to_have_count(5)

    # The most important part: Take a screenshot for visual verification.
    page.screenshot(path="jules-scratch/verification/market_influence_verification.png")

    print("Verification script ran successfully. Screenshot created.")
    browser.close()

with sync_playwright() as playwright:
    run_verification(playwright)