# Corner Shop Tycoon (Placeholder Title)

Welcome to Corner Shop Tycoon! You are the new manager of a local corner shop. Your job is to keep the shelves stocked, read the market, and turn a tidy profit. Can you grow your humble shop into a local empire by mastering the daily ebb and flow of supply and demand?

## ✨ Features

*   **Deep Economic Simulation:** A dynamic market with daily events, weekly forecasts, and fluctuating customer demand. Don't just react—predict!
*   **Strategic Shop Management:** Buy low, sell high. Manage your inventory, unlock more storage space, and reinvest your earnings to grow your business.
*   **Unique Companion Mode:** Team up with a friend! One person plays on the main screen (the "Host"), while another uses their phone or tablet as a dedicated controller (the "Companion"). The companion gets full access to the in-game phone/clipboard for managing stock, checking reports, and making purchases, allowing for seamless cooperative play.
*   **In-depth Reporting:** From the Manager's daily recommendations to a "deep dive" DEV report, you have all the data you need to make informed decisions. You can even simulate and view market reports for future days!
*   **Unlockable Content:** Use your hard-earned Shop Points to purchase permanent upgrades like expanded storage and new art styles for your shop.

## 🚀 How to Play

**Setup:** Simply open the `index.html` file in a modern web browser. No server or installation is required.

### Single Player

1.  Click **New Game**.
2.  Choose your desired game settings through the setup screens.
3.  Click **Start Game** and begin your career!

### Companion Mode (2 Players)

This mode allows a second player to manage the shop's logistics on a separate device.

1.  **Host (Main Screen):**
    *   Start a new game as normal.
    *   On the "New Game" screen, a **Host ID** will be displayed. Share this ID with your companion.
    *   Wait for the companion to connect. You'll see a "Connected!" message.

2.  **Companion (Phone/Tablet):**
    *   Open the same `index.html` file on your device (e.g., a phone or tablet).
    *   Instead of starting a new game, enter the **Host ID** provided by the host.
    *   Click **Connect**. Your screen will become the in-game controller, giving you access to the phone/clipboard UI.

## 📖 Gameplay Guide

*   **The Goal:** Your primary goal is to make money (`Cash`) and earn `Shop Points` by successfully serving customers.
*   **The Loop:**
    1.  **Start the Day:** Check the **Manager's Report** and the **Newspaper** for hints about what's cheap or in-demand today. Use the reports to plan your purchases.
    2.  **Stock Up:** Use the phone/clipboard UI to buy items. Try to buy items that are trading for less than their base cost.
    3.  **Serve Customers:** Customers will appear in your shop with requests. If you have the item in stock, you'll automatically sell it.
    4.  **End the Day:** When you're ready, end the day to see a report summarizing your profits and performance.
    5.  **Repeat:** A new day brings new market changes! Adapt your strategy to the 7-day forecast.

*   **The Phone/Clipboard:** This is your command center. From here you can:
    *   **Restock:** Buy new inventory for your shelves.
    *   **Unlocks:** Spend Shop Points on permanent upgrades.
    *   **Reports:** View detailed market analysis, including multi-day forecasts and simulated reports for future days.

## 🛠️ For Developers

*   **Architecture:** The entire game—logic, UI, and styling—is contained within `index.html`. This makes it highly portable and easy to run.
*   **Companion Mode Tech:** The cooperative mode is powered by **PeerJS**, which facilitates a direct WebRTC (peer-to-peer) connection between the host and companion browsers, enabling real-time, low-latency communication.
*   **DEV Mode:** On the new game screen, you can enable "DEV Mode". This provides access to a special DEV Report with more granular data, useful for debugging and deeply understanding the market simulation.