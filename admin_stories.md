**Title:**
_An admin, I want to log into the portal with my username and password, so that I can manage the platform securely._
**Acceptance Criteria:**
1. Admin can enter a valid username and password.
2. System verifies credentials and redirects to the admin dashboard.
3. Unsuccessful login attempts display an appropriate error message.
**Priority:** High
**Story Points:** 5
**Notes:**
- Passwords should be stored securely (hashed & salted).

**Title:**
_An admin, I want to log out of the portal, so that I can protect system access._
**Acceptance Criteria:**
1. Admin can click a logout button.
2. Session is terminated and user is redirected to the login page.
3. No residual authentication tokens remain in local storage or cookies.
**Priority:** High
**Story Points:** 3
**Notes:**
- Logout should be available on all admin pages.

**Title:**
_An admin, I want to add doctors to the portal, so that I can expand the service roster._
**Acceptance Criteria:**
1. Admin can open an “Add Doctor” form.
2. Form validates required fields (name, specialty, contact info).
3. New doctor record is persisted in the database and visible in the doctor list.
**Priority:** Medium
**Story Points:** 8
**Notes:**
- Ensure duplicate email or license numbers are prevented.

**Title:**
_An admin, I want to delete a doctor's profile from the portal, so that I can remove inactive or invalid entries._
**Acceptance Criteria:**
1. Admin can select a doctor and confirm deletion.
2. System removes the doctor record from the database after confirmation.
3. Deletion updates all related views and logs the action.
**Priority:** Medium
**Story Points:** 5
**Notes:**
- Use soft‑delete to preserve audit history.

**Title:**
_An admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month and track usage statistics, so that I can analyze platform usage._
**Acceptance Criteria:**
1. Admin can execute the stored procedure via MySQL CLI.
2. Procedure returns monthly appointment counts.
3. Results are displayed in a readable format (e.g., CSV or table) and can be exported.
**Priority:** Low
**Story Points:** 5
**Notes:**
- Ensure proper permissions are set for executing the procedure.
