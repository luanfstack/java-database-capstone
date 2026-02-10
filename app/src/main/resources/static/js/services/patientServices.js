import { API_BASE_URL } from "../config/config.js";
const PATIENT_API = API_BASE_URL + "/patient";

export async function patientSignup(data) {
  try {
    const response = await fetch(`${PATIENT_API}`, {
      method: "POST",
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify(data),
    });
    const result = await response.json();
    if (!response.ok) {
      throw new Error(result.message || "Signup failed");
    }
    return {
      success: response.ok,
      message: result.message,
      patient: result.patient,
    };
  } catch (error) {
    console.error("Error :: patientSignup :: ", error);
    return { success: false, message: error.message };
  }
}

export async function patientLogin(data) {
  try {
    console.log("patientLogin :: ", data);
    const response = await fetch(`${PATIENT_API}/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(result.message || "Login failed");
    }

    return {
      success: response.ok,
      message: result.message,
      token: result.token,
      patient: result.patient,
    };
  } catch (error) {
    console.error("Error :: patientLogin :: ", error);
    return { success: false, message: error.message };
  }
}

export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/${token}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await response.json();

    if (response.ok) {
      return data.patient;
    }

    return null;
  } catch (error) {
    console.error("Error fetching patient details:", error);
    return null;
  }
}

export async function getPatientAppointments(id, token, user) {
  try {
    const response = await fetch(`${PATIENT_API}/${id}/${user}/${token}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    const data = await response.json();

    if (response.ok) {
      return data.appointments;
    }

    return null;
  } catch (error) {
    console.error("Error fetching patient details:", error);
    return null;
  }
}

export async function filterAppointments(condition, name, token) {
  try {
    const response = await fetch(
      `${PATIENT_API}/filter/${condition}/${name}/${token}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      },
    );

    if (response.ok) {
      const data = await response.json();
      return data;
    } else {
      console.error("Failed to fetch appointments:", response.statusText);
      return { appointments: [] };
    }
  } catch (error) {
    console.error("Error filtering appointments:", error);
    alert("Something went wrong while filtering appointments!");
    return { appointments: [] };
  }
}

export async function updatePatientProfile(id, data, token) {
  try {
    const response = await fetch(`${PATIENT_API}/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(data),
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(result.message || "Profile update failed");
    }

    return {
      success: response.ok,
      message: result.message,
      patient: result.patient,
    };
  } catch (error) {
    console.error("Error updating patient profile:", error);
    return { success: false, message: error.message };
  }
}

export async function deletePatientAccount(id, token) {
  try {
    const response = await fetch(`${PATIENT_API}/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(result.message || "Account deletion failed");
    }

    return { success: response.ok, message: result.message };
  } catch (error) {
    console.error("Error deleting patient account:", error);
    return { success: false, message: error.message };
  }
}
