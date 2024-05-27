package wiprojss24gr7.service;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import wiprojss24gr7.database.DatabaseManager;

public class UserService {
	
	private static List<Integer> SL = new ArrayList<>();
	private static List<Integer> SLNoTutor = new ArrayList<>();
	private static List<Integer> PL = new ArrayList<>();
	
	public static void populateUserList(DefaultListModel<String> listModel, String listType) {
        List<String> Names = DatabaseManager.getUsers(listType);
        listModel.clear();
        for (String user : Names) {
            listModel.addElement(user);
        }
    }
	
	public static void populateStudentNoTutorList(DefaultListModel<String> listModel, String listType) {
        List<String> Names = DatabaseManager.getStudentenWithNoTutor();
        listModel.clear();
        for (String user : Names) {
            listModel.addElement(user);
        }
    }

	public static List<Integer> getSL() {
		return SL;
	}
	
	public static int getSLIndex(int i) {
		return SL.get(i);
	}
	
	public static int getSLNoTutorIndex(int i) {
		return SLNoTutor.get(i);
	}

	public static void addSL(int pk) {
		SL.add(pk);
	}

	public static void delSL() {
		SL.clear();
	}
	
	public static void addSLNoTutor(int pk) {
		SLNoTutor.add(pk);
	}
	
	public static void addPL(int pk) {
		PL.add(pk);
	}
	
	public static int getPLIndex(int i) {
		return PL.get(i);
	}


	public static List<Integer> getSLNoTutor() {
		return SLNoTutor;
	}

	public static void setSLNoTutor(List<Integer> sLNoTutor) {
		SLNoTutor = sLNoTutor;
	}

	public static List<Integer> getPL() {
		return PL;
	}

	public static void setPL(List<Integer> pL) {
		PL = pL;
	}
	
}

