const oFetchAddReviewNS = require("/d/Programming/eclipse-workspace/ReviewssiteFinal/src/main/resources/static/js/fetch-add-review.js");

/**** Everything to test
 * Does the post-POST function add Partial feedback to DOM
 * Does the click event listener trigger when click happens
 * does click-event listener add post-POST to Response
 * does click-event listener add form fields to fetch Header
 * does fetch target correct path
 * 
 */

// Does the post-POST function add Partial feedback to DOM, empty return ex.
const partialReturnMock = jest.fn();
test(
    "Does the post-POST function add Partial feedback to DOM, empty return ex.",
    () => {
        const initialBodyHtml  = '<table class="game-reviews"><tr><td><img></td><td></td></tr></table><form id="add-review"><label for="add-review-title">You may give your review a title: </label><input type="text" id="add-review-title" /><br /><label for="add-review-author">Who is writing this great review: </label><input type="text" id="add-review-author" /><br /><label for="add-review-content">Review: </label><textarea id="add-review-content"></textarea><button >Submit Review</button></form>';
        const oFormValues = {
            addReviewTitle: ``,
            addReviewAuthor: ``,
            addReviewContent: ``
        };
        document.body.innerHTML = initialBodyHtml;
        document.getElementById(`add-review-author`).innerText = oFormValues[addReviewAuthor];
        document.getElementById(`add-review-title`).innerText = oFormValues[addReviewTitle];
        document.getElementById(`add-review-content`).innerText = oFormValues[addReviewContent];
        const formSubmission = {};
        const expectedSecondText = document.body.innerHTML;
        const mockPayload = new Event("click");
        document.querySelector(`#add-review button`).addEventListener(`click`, oFetchAddReviewNS.addReview);
        mockPayload.initEvent(`click`);
        expect(oFetchAddReviewNS.addReview).toBeCalled();
        expect(document.querySelector(`#add-review button`).innerHTML).toEqual(expectedSecondText);
    }
);

    
    

// non-empty return
`<tr class="new-entry"><td>Thank you for your submission!</td></tr>`;
// partialReturnMock.

/*var event = new Event('build');

// Listen for the event.
elem.addEventListener('build', function (e) {  }, false);

// Dispatch the event.
elem.dispatchEvent(event);*/