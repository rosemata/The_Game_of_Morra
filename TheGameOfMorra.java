import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TheGameOfMorra extends Application {
	
	private Pane myPane, secondPane;
	private VBox myVBox, secondVBox;
	private TextField myTextField;
	private Button myButton;
	private ListView<String> listView;
	private Text myText;
	private EventHandler<ActionEvent> toTheStats;
	private int portNumber;
	private ObservableList<String> data;
	@SuppressWarnings("unused")
	private Server serverConnection;
	
	public Scene startScene() {
		// VBOX on the main scene
		myTextField = new TextField("5555");
		myButton = new Button("TURN ON THE SERVER");
		myButton.setOnAction(toTheStats);
		
		myVBox = new VBox(25, myTextField,myButton);
		myVBox.setLayoutX(220);
		myVBox.setLayoutY(200);
		myVBox.setAlignment(Pos.BASELINE_CENTER);
		// Main Pane 
		myPane = new Pane(myVBox);
		// Background
		Image image = new Image("background.jpg");
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
												BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false));
		Background background = new Background(backgroundImage);
		myPane.setBackground(background);
		return new Scene(myPane,600,600);
	}
	
	public Scene secondScene() {
		myText = new Text("Game Stats");
		myText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		myText.setFill(Color.WHITE);
		listView = new ListView<>();
		
		data = FXCollections.observableArrayList (
				"Game will start with 2 players");

		
		listView.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
		listView.getItems().addAll(data);
		secondVBox = new VBox(50, myText, listView);
		secondVBox.setLayoutX(180);
		secondVBox.setLayoutY(50);
		secondVBox.setAlignment(Pos.CENTER);
		secondPane = new Pane(secondVBox);
		
		Image image = new Image("background_2.jpg");
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
												BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(1.0, 1.0, true, true, false, false));
		Background background = new Background(backgroundImage);
		secondPane.setBackground(background);
		return new Scene(secondPane, 600,600);
	}

	public boolean isValidPort(String port) {
		for(char x : port.toCharArray()) {
			if(!Character.isDigit(x)) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(Server) Let's Play Morra!!!");
		
		// HASHMAP FOR SCENES
		HashMap<String, Scene> sceneMap = new HashMap<String, Scene>();
		sceneMap.put("secondScene", secondScene());
		
		toTheStats = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(isValidPort(myTextField.getText())) {
					portNumber = Integer.parseInt(myTextField.getText());
					primaryStage.setScene(sceneMap.get("secondScene"));

					serverConnection = new Server(data->{
						Platform.runLater(()-> {
							listView.getItems().clear();
							listView.getItems().add(data.toString());
						});
					},portNumber);
				}
					
				else {
					myTextField.setText("Port Number is Invalid !");
				}
			}
		};
		
		sceneMap.put("background", startScene());
		primaryStage.setScene(sceneMap.get("background"));
		primaryStage.show();
	}
}
