import { showBookingOverlay } from "../loggedPatient.js";
import { deleteDoctor } from "../services/doctorServices.js";
import { getPatientData } from "../services/patientServices.js";

export function createDoctorCard(doctor) {
  const card = document.createElement("div");
  card.classList.add("doctor-card");

  const info = document.createElement("div");
  info.classList.add("doctor-info");

  const nameEl = document.createElement("h3");
  nameEl.textContent = doctor.name;

  const specialtyEl = document.createElement("p");
  specialtyEl.textContent = `Specialty: ${doctor.specialty}`;

  const emailEl = document.createElement("p");
  emailEl.textContent = `Email: ${doctor.email}`;

  const timesContainer = document.createElement("div");
  timesContainer.classList.add("available-times");
  const timesHeader = document.createElement("p");
  timesHeader.textContent = "Available Times:";
  const timesList = document.createElement("ul");
  doctor.availableTimes.forEach((time) => {
    const li = document.createElement("li");
    li.textContent = time;
    timesList.appendChild(li);
  });
  timesContainer.appendChild(timesHeader);
  timesContainer.appendChild(timesList);

  info.appendChild(nameEl);
  info.appendChild(specialtyEl);
  info.appendChild(emailEl);
  info.appendChild(timesContainer);

  const actions = document.createElement("div");
  actions.classList.add("doctor-actions");

  const role = localStorage.getItem("userRole");

  if (role === "admin") {
    const deleteBtn = document.createElement("button");
    deleteBtn.textContent = "Delete";
    deleteBtn.classList.add("btn-delete");
    deleteBtn.addEventListener("click", async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("You must be logged in as admin to delete doctors.");
        return;
      }
      const { success, message } = await deleteDoctor(doctor.id, token);
      alert(message);
      if (success) {
        card.remove();
      }
    });
    actions.appendChild(deleteBtn);
  } else if (role === "patient") {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.classList.add("btn-book");
    bookBtn.addEventListener("click", async (e) => {
      const token = localStorage.getItem("token");
      if (!token) {
        alert("You must be logged in to book an appointment.");
        return;
      }
      const patient = await getPatientData(token);
      if (!patient) {
        alert("Unable to retrieve patient data.");
        return;
      }
      showBookingOverlay(e, doctor, patient);
    });
    actions.appendChild(bookBtn);
  } else {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    bookBtn.classList.add("btn-book");
    bookBtn.addEventListener("click", () => {
      alert("Please log in to book an appointment.");
    });
    actions.appendChild(bookBtn);
  }

  card.appendChild(info);
  card.appendChild(actions);

  return card;
}
