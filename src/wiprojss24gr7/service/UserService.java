package wiprojss24gr7.service;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import wiprojss24gr7.database.DatabaseManager;

public class UserService {
	
	private static List<Integer> SL = new ArrayList<>();
	private static List<Integer> SLNoTutor = new ArrayList<>();
	private static List<Integer> PL = new ArrayList<>();
	private static List<Integer> SLMyStudents = new ArrayList<>();
	
	public static void populateUserList(DefaultListModel<String> listModel, String listType, boolean noTutor) {
	    List<String> names = DatabaseManager.getUsers(listType, noTutor);
	    listModel.clear();
	    for (String user : names) {
	        listModel.addElement(user);
	    }
	}
	
    //Methoden zu Studenten Listen
    public static List<Integer> getSL() {
        return SL;
    }

    public static void addSL(int pk) {
        SL.add(pk);
    }

    public static int getSLIndex(int i) {
        return SL.get(i);
    }

    public static List<Integer> getSLNoTutor() {
        return SLNoTutor;
    }

    public static void setSLNoTutor(List<Integer> sLNoTutor) {
        SLNoTutor = sLNoTutor;
    }

    public static void addSLNoTutor(int pk) {
        SLNoTutor.add(pk);
    }

    public static int getSLNoTutorIndex(int i) {
        return SLNoTutor.get(i);
    }

    // Methodes zu Professor Listen
    public static List<Integer> getPL() {
        return PL;
    }

    public static void setPL(List<Integer> pL) {
        PL = pL;
    }

    public static void addPL(int pk) {
        PL.add(pk);
    }

    public static int getPLIndex(int i) {
        return PL.get(i);
    }

    public static List<Integer> getSLMyStudents() {
        return SLMyStudents;
    }

    public static void setSLMyStudents(List<Integer> sLMyStudents) {
        SLMyStudents = sLMyStudents;
    }

    public static void addSLMyStudents(int pk) {
        System.out.println("Something happens with SLMyStudents here");
        SLMyStudents.add(pk);
    }

    public static int getSLMyStudentsIndex(int i) {
        return SLMyStudents.get(i);
    }

    //Alle Listen leeren
    public static void delAll() {
        SL.clear();
        SLNoTutor.clear();
        PL.clear();
        SLMyStudents.clear();
    }
}

