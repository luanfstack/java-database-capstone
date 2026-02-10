import { getAllAppointments } from "./services/appointmentRecordService.js";

import { createPatientRow } from "./components/patientRows.js";

const tableBody = document.querySelector("#patientTableBody");

let selectedDate = new Date().toISOString().split("T")[0];

const token = localStorage.getItem("token");

let patientName = null;

document.getElementById("searchBar").addEventListener("input", (event) => {
  const inputValue = event.target.value.trim();

  if (inputValue) {
    patientName = inputValue;
  } else {
    patientName = null;
  }

  loadAppointments();
});

document.getElementById("todayButton").addEventListener("click", () => {
  selectedDate = new Date().toISOString().split("T")[0];

  document.getElementById("datePicker").value = selectedDate;

  loadAppointments();
});

document.getElementById("datePicker").addEventListener("change", (event) => {
  selectedDate = event.target.value;

  loadAppointments();
});

async function loadAppointments() {
  try {
    const appointments = await getAllAppointments(
      selectedDate,
      patientName,
      token,
    );

    tableBody.innerHTML = "";

    if (appointments.length === 0) {
      const messageRow = document.createElement("tr");
      messageRow.innerHTML = `
        <td colspan="5" class="text-center">No Appointments found for today.</td>
      `;
      tableBody.appendChild(messageRow);
    } else {
      appointments.forEach((appointment) => {
        const patient = {
          id: appointment.patientId,
          name: appointment.patientName,
          phone: appointment.patientPhone,
          email: appointment.patientEmail,
        };

        const row = createPatientRow(
          patient,
          appointment.id,
          appointment.doctorId,
        );

        tableBody.appendChild(row);
      });
    }
  } catch (error) {
    console.error("Error loading appointments:", error);

    tableBody.innerHTML = "";
    const errorRow = document.createElement("tr");
    errorRow.innerHTML = `
      <td colspan="5" class="text-center">Error loading appointments. Try again later.</td>
    `;
    tableBody.appendChild(errorRow);
  }
}

document.addEventListener("DOMContentLoaded", () => {
  loadAppointments();
});
