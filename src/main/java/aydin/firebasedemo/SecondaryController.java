package aydin.firebasedemo;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SecondaryController {

    @FXML
    private TextField rNameTextField;

    @FXML
    private TextField lNameTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private PasswordField rPasswordTextField;

    @FXML
    private PasswordField lPasswordTextField;

    @FXML
    private Label rResultText;

    @FXML
    private Label lResultText;

    private ObservableList<Person> listOfUsers = FXCollections.observableArrayList();
    private Person person;

    @FXML
    private void switchToPrimary() throws IOException { DemoApp.setRoot("primary"); }

    @FXML
    private void registerUser() throws IOException {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(emailTextField.getText())
                    .setEmailVerified(false)
                    .setPassword(rPasswordTextField.getText())
                    .setPhoneNumber("+1" + phoneTextField.getText())
                    .setDisplayName(rNameTextField.getText())
                    .setDisabled(false);

            UserRecord userRecord;
            userRecord = DemoApp.fauth.createUser(request);
            rResultText.setText("Successfully created new user with Firebase Uid: " + userRecord.getUid()
                + " check Firebase > Authentication > Users tab");

            DocumentReference docRef = DemoApp.fstore.collection("Persons").document(UUID.randomUUID().toString());

            Map<String, Object> data = new HashMap<>();
            data.put("Name", rNameTextField.getText());
            data.put("Age", Integer.parseInt(ageTextField.getText()));
            data.put("Email", emailTextField.getText());
            data.put("Phone", phoneTextField.getText());
            data.put("Password", rPasswordTextField.getText());

            //asynchronously write data
            ApiFuture<WriteResult> result = docRef.set(data);

            rResultText.setText("Successfully registered! Please log in.");

        }
        catch (FirebaseAuthException ex) {
            System.out.println("PROBLEM: " + ex.getMessage());
            rResultText.setText("Error creating a new user in the firebase");
        }
    }

    @FXML
    private void loginUser(){
        //asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future =  DemoApp.fstore.collection("Persons").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(!documents.isEmpty()) {
                System.out.println("Getting (reading) data from firabase database....");
                listOfUsers.clear();
                for (QueryDocumentSnapshot document : documents) {
                    String username = document.getString("Name");
                    String password = document.getString("Password");
                    if (lNameTextField.getText().equals(username) && lPasswordTextField.getText().equals(password)) {
                        System.out.println("yupppp " + username);
                        DemoApp.setRoot("primary");
                        return;
                    }
                    else { System.out.println("nope..."); }
                }
            }
            else {
                System.out.println("No data");
                lResultText.setText("Email not registered");
                return;
            }
            System.out.println("Name or Password incorrect");
            lResultText.setText("Name or Password incorrect");
        }
        catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }


    }
}