# Firebase Storage Structure

**Note:** This document outlines the **intended** structure for storing high-resolution reference imagery in Firebase Storage. This is not the current implementation.

## Directory Structure

The images will be organized in a hierarchical structure to allow for easy retrieval based on the item and checklist question. The proposed structure is as follows:

`/images/{brand}/{category}/{model}/{question_id}/{image_type}.jpg`

### Placeholders

*   `{brand}`: The brand of the item (e.g., `chanel`).
*   `{category}`: The category of the item (e.g., `handbag`).
*   `{model}`: The model of the item (e.g., `classic_flap`).
*   `{question_id}`: A unique identifier for the question in the checklist (e.g., `cc_lock`). This should correspond to a field in the checklist JSON file.
*   `{image_type}`: The type of image, which can be either `authentic` or `inauthentic`.

### Example

For a Chanel Classic Flap handbag, the images related to the "CC lock" question would be stored as follows:

*   Authentic image: `/images/chanel/handbag/classic_flap/cc_lock/authentic.jpg`
*   Inauthentic image: `/images/chanel/handbag/classic_flap/cc_lock/inauthentic.jpg`

This structure will allow the application to dynamically construct the image URL based on the selected item and the current question in the diagnostic funnel.
