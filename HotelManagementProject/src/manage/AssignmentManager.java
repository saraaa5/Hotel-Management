package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.CleanupAssignment;
import entity.Reservation;
import entity.Role;
import entity.Room;
import entity.Worker;

public class AssignmentManager {

	private String assignemntFilename;
	private AdministratorManager workerManager;
	private RoomManager  roomManager;
	private List<CleanupAssignment> assignments = new ArrayList<CleanupAssignment>();
	public AssignmentManager(String assignmentFilename, AdministratorManager workerManager, RoomManager roomManager) {
	      this.workerManager = workerManager;
	      this.assignemntFilename = assignmentFilename;
	      this.roomManager = roomManager;
  }

  public int getNumberOfAssignments(LocalDate date, Worker worker) {
    int count = 0;
    for (CleanupAssignment assignment : assignments) {
      if (assignment.getDate().equals(date) && assignment.getMaidId().equals(worker.getKorisnickoIme())) {
        count++;
      }
    }
    return count;
	}


  public int getLastMonthCount(String id) {
    LocalDate date = LocalDate.now().plusDays(1);
    LocalDate lastMonth = date.minusMonths(1);
    int count = 0;
    for (CleanupAssignment assignment : assignments) {
      if (assignment.getMaidId().equals(id)
          && assignment.getDate().isAfter(lastMonth) && assignment.getDate().isBefore(date)) {
        count++;
      }
    }
    return count;
  }

  public void assignHousekeeper(Room room, Reservation res) {
	List<Worker> workers= workerManager.getWorkers();
	List<Worker> housekeepers = new ArrayList<Worker>();

	for (Worker worker : workers) {
      if (worker.getRole().equals(Role.MAID)) {
        housekeepers.add(worker);
      }
	}

	LocalDate date = LocalDate.now();

	int minForCleanup = 10000;
	Worker selected = null;
    for (Worker worker : housekeepers) {
      if (getNumberOfAssignments(date, worker) < minForCleanup) {
    	  minForCleanup = getNumberOfAssignments(date, worker);
    	  selected = worker;
      }
    }
    CleanupAssignment assignment = new CleanupAssignment(selected.getKorisnickoIme(), room.getBrojSobe(), res.getUUID() , date);

	addAssignment(assignment);
  }

  public void loadData() {
	     // TODO implement
	  BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(assignemntFilename));
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        CleanupAssignment assignment = new CleanupAssignment(parts[1], Integer.valueOf(parts[2]), parts[3], LocalDate.parse(parts[4]));
        boolean isCompleted = Boolean.parseBoolean(parts[4]);
        assignment.setIsCompleted(isCompleted);
        assignment.setId(parts[0]);
        assignments.add(assignment);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }



  public List<CleanupAssignment> getAssignments() {
    return assignments;
  }

  public void completeAssignment(CleanupAssignment assignment) {

	  for (CleanupAssignment i : assignments) {
      if (i.getId().equals(assignment.getId())) {
        i.setIsCompleted(true);
      }
    }
    saveData();


    List<Room> rooms = roomManager.getRooms();
    for (Room room : rooms) {
      if (room.getBrojSobe() == assignment.getRoomId()) {
        room.setStatusSobe(Room.SobaStatus.SLOBODNA);
      }
    }

    roomManager.saveData();
  }

  public List<CleanupAssignment> getAssignmentsForHousekeeper(String id, LocalDate date) {
    List<CleanupAssignment> housekeeperAssignments = new ArrayList<CleanupAssignment>();

    for (CleanupAssignment i : assignments) {
      if (i.getMaidId().equals(id) && i.getDate().isEqual(date) && !i.getIsCompleted()) {
        housekeeperAssignments.add(i);
      }
    }
    return housekeeperAssignments;
  }
  public void addAssignment(CleanupAssignment assignment) {
    assignments.add(assignment);
   saveData();
  }

  public void saveData() {
    PrintWriter pw = null;
    try {
      pw = new PrintWriter(new FileWriter(this.assignemntFilename, false));
      for (CleanupAssignment i : assignments) {
        pw.println(i.toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (pw != null) {
        pw.close();
      }
    }
  }
}
