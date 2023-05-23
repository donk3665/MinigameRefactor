package Stages;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class RhySongSelectScene extends MasterScene{

    @Override
    Scene run(Stage primaryStage, SceneTransferData data)  {
        //scene counter
        //background image and layout setup
        Image BackgroundImage = new Image("buttonImages/gameSelect/GameBackground.png", 1536*widthAdjust, 864*heightAdjust, false, false);
        ImageView backgroundImage = new ImageView(BackgroundImage);
        backgroundImage.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundImage.fitHeightProperty().bind(primaryStage.heightProperty());

        Image box = new Image("buttonImages/otherAssets/Box.png", 350*widthAdjust, 90*heightAdjust, false, false);
        GridPane grid = new GridPane();
        Font basic = new Font("Verdana", 12*widthAdjust);


        //24 buttons with 24 labels going to 24 different rhythm game maps

        Label temp = new Label("BlackYooh vs. siromaru - BLACK or WHITE [BASIC]");
        temp.setFont(basic);
        StackPane stack1 = new StackPane();
        stack1.getChildren().addAll(new ImageView(box),temp );
        Button songSelect1 = new Button("",stack1);

        temp =new Label("Porter Robinson & Madeon - Shelter [Easy]");
        temp.setFont(basic);
        StackPane stack2 = new StackPane();
        stack2.getChildren().addAll(new ImageView(box), temp);
        Button songSelect2 = new Button("",stack2);

        temp = new Label("t+pazolite - QLWA [Normal]");
        temp.setFont(basic);
        StackPane stack3 = new StackPane();
        stack3.getChildren().addAll(new ImageView(box),temp);
        Button songSelect3 = new Button("",stack3);

        temp =  new Label("NOMA - PEPSI  MAN [NORMAL MAN]");
        temp.setFont(basic);
        StackPane stack4 = new StackPane();
        stack4.getChildren().addAll(new ImageView(box),temp);
        Button songSelect4 = new Button("",stack4);

        temp = new Label("KANA-BOON - Silhouette [EZ]");
        temp.setFont(basic);
        StackPane stack5 = new StackPane();
        stack5.getChildren().addAll(new ImageView(box),temp );
        Button songSelect5 = new Button("",stack5);

        temp = new Label("MAKOOTO - Tanukichi no Bouken [Easy]");
        temp.setFont(basic);
        StackPane stack6 = new StackPane();
        stack6.getChildren().addAll(new ImageView(box),temp );
        Button songSelect6 = new Button("",stack6);

        temp = new Label("Lindsey Stirling - Senbonzakura [Harby's NM]");
        temp.setFont(basic);
        StackPane stack7 = new StackPane();
        stack7.getChildren().addAll(new ImageView(box), temp);
        Button songSelect7 = new Button("",stack7);

        temp = new Label("Jin ft. MARiA from GARNiDELiA - daze [NM]");
        temp.setFont(basic);
        StackPane stack8 = new StackPane();
        stack8.getChildren().addAll(new ImageView(box), temp);
        Button songSelect8 = new Button("",stack8);

        temp = new Label("Fairouz Ai & Ishikawa Kaito - Onegai Muscle [Easy]");
        temp.setFont(basic);
        StackPane stack9 = new StackPane();
        stack9.getChildren().addAll(new ImageView(box), temp);
        Button songSelect9 = new Button("",stack9);

        temp = new Label("Soleily - Renatus [Normal]");
        temp.setFont(basic);
        StackPane stack10 = new StackPane();
        stack10.getChildren().addAll(new ImageView(box), temp);
        Button songSelect10 = new Button("",stack10);

        temp = new Label("Soleily - Renatus [Hard]");
        temp.setFont(basic);
        StackPane stack11 = new StackPane();
        stack11.getChildren().addAll(new ImageView(box), temp );
        Button songSelect11 = new Button("",stack11);

        temp = new Label("TK from Ling tosite sigure - unravel [Normal]");
        temp.setFont(basic);
        StackPane stack12 = new StackPane();
        stack12.getChildren().addAll(new ImageView(box), temp);
        Button songSelect12 = new Button("",stack12);

        temp = new Label("BlackYooh vs. siromaru - BLACK or WHITE [NOVICE]");
        temp.setFont(basic);
        StackPane stack13 = new StackPane();
        stack13.getChildren().addAll(new ImageView(box), temp);
        Button songSelect13 = new Button("",stack13);

        temp = new Label("C-Show - Invitation from Mr.C [NOVICE]");
        temp.setFont(basic);
        StackPane stack14 = new StackPane();
        stack14.getChildren().addAll(new ImageView(box), temp);
        Button songSelect14 = new Button("",stack14);

        temp = new Label("DJ OKAWARI - Flower Dance [Normal]");
        temp.setFont(basic);
        StackPane stack15 = new StackPane();
        stack15.getChildren().addAll(new ImageView(box), temp);
        Button songSelect15 = new Button("",stack15);

        temp = new Label("Fairouz Ai & Ishikawa Kaito - Onegai Muscle[Normal]");
        temp.setFont(basic);
        StackPane stack16 = new StackPane();
        stack16.getChildren().addAll(new ImageView(box), temp);
        Button songSelect16 = new Button("",stack16);

        temp = new Label("KANA-BOON - Silhouette [NM]");
        temp.setFont(basic);
        StackPane stack17 = new StackPane();
        stack17.getChildren().addAll(new ImageView(box), temp);
        Button songSelect17 = new Button("",stack17);

        temp = new Label("MAKOOTO - Tanukichi no Bouken [Leni's Normal]");
        temp.setFont(basic);
        StackPane stack18 = new StackPane();
        stack18.getChildren().addAll(new ImageView(box), temp);
        Button songSelect18 = new Button("",stack18);

        temp = new Label("Kairiki bear - Inai Inai Izonshou [timing hell]");
        temp.setFont(basic);
        StackPane stack19 = new StackPane();
        stack19.getChildren().addAll(new ImageView(box), temp);
        Button songSelect19 = new Button("",stack19);

        temp = new Label("BlackYooh vs. siromaru - BLACK or WHITE [ADVANCED]");
        temp.setFont(basic);
        StackPane stack20 = new StackPane();
        stack20.getChildren().addAll(new ImageView(box), temp );
        Button songSelect20 = new Button("",stack20);

        temp = new Label("Porter Robinson & Madeon - Shelter [Loneliness]");
        temp.setFont(basic);
        StackPane stack21 = new StackPane();
        stack21.getChildren().addAll(new ImageView(box), temp);
        Button songSelect21 = new Button("",stack21);

        temp = new Label("Soleily - Renatus [Insane]");
        temp.setFont(basic);
        StackPane stack22 = new StackPane();
        stack22.getChildren().addAll(new ImageView(box), temp);
        Button songSelect22 = new Button("",stack22);

        temp = new Label("LeaF - I [Limbo]");
        temp.setFont(basic);
        StackPane stack23 = new StackPane();
        stack23.getChildren().addAll(new ImageView(box), temp);
        Button songSelect23 = new Button("",stack23);

        temp = new Label("Soulja Baka - Soulja Baka [SMITH RMX]");
        temp.setFont(basic);
        StackPane stack24 = new StackPane();
        stack24.getChildren().addAll(new ImageView(box), temp);
        Button songSelect24 = new Button("",stack24);

        //setting style of buttons
        songSelect1.setStyle("-fx-background-color: transparent;");
        songSelect2.setStyle("-fx-background-color: transparent;");
        songSelect3.setStyle("-fx-background-color: transparent;");
        songSelect4.setStyle("-fx-background-color: transparent;");
        songSelect5.setStyle("-fx-background-color: transparent;");
        songSelect6.setStyle("-fx-background-color: transparent;");
        songSelect7.setStyle("-fx-background-color: transparent;");
        songSelect8.setStyle("-fx-background-color: transparent;");
        songSelect9.setStyle("-fx-background-color: transparent;");
        songSelect10.setStyle("-fx-background-color: transparent;");
        songSelect11.setStyle("-fx-background-color: transparent;");
        songSelect12.setStyle("-fx-background-color: transparent;");
        songSelect13.setStyle("-fx-background-color: transparent;");
        songSelect14.setStyle("-fx-background-color: transparent;");
        songSelect15.setStyle("-fx-background-color: transparent;");
        songSelect16.setStyle("-fx-background-color: transparent;");
        songSelect17.setStyle("-fx-background-color: transparent;");
        songSelect18.setStyle("-fx-background-color: transparent;");
        songSelect19.setStyle("-fx-background-color: transparent;");
        songSelect20.setStyle("-fx-background-color: transparent;");
        songSelect21.setStyle("-fx-background-color: transparent;");
        songSelect22.setStyle("-fx-background-color: transparent;");
        songSelect23.setStyle("-fx-background-color: transparent;");
        songSelect24.setStyle("-fx-background-color: transparent;");
        SceneTransferData transferData = new SceneTransferData();
        //event handlers for all 24 buttons
        songSelect1.setOnAction(event->{
            menuMusic.stop();
            transferData.setFilename("BlackYooh vs. siromaru - BLACK or WHITE (DE-CADE) [Usagi's BASIC Lv.6].osu");
            controller.changeScenes(SceneEnums.RHY_GAME, transferData);
        });
        songSelect2.setOnAction(event->{
            menuMusic.stop();
            transferData.setFilename("Porter Robinson & Madeon - Shelter (Dellvangel) [Easy].osu");
            controller.changeScenes(SceneEnums.RHY_GAME, transferData);
        });
        songSelect3.setOnAction(event->{
            menuMusic.stop();
            transferData.setFilename("t+pazolite - QLWA (Yugu) [ExNeko's Normal].osu");
            controller.changeScenes(SceneEnums.RHY_GAME, transferData);
        });
        songSelect4.setOnAction(event->{
            menuMusic.stop();
            transferData.setFilename("NOMA - PEPSI  MAN (Zetera) [HD MAN].osu");
            controller.changeScenes(SceneEnums.RHY_GAME, transferData);
        });
        songSelect5.setOnAction(event->{
            menuMusic.stop();
            transferData.setFilename("KANA-BOON - Silhouette (ExKagii-) [Kyou's EZ].osu");
            controller.changeScenes(SceneEnums.RHY_GAME, transferData);
        });
        songSelect6.setOnAction(event->{
            menuMusic.stop();
            transferData.setFilename("MAKOOTO - Tanukichi no Bouken (Xinely) [Leni's Easy].osu");
            controller.changeScenes(SceneEnums.RHY_GAME, transferData);
        });
        songSelect7.setOnAction(event->{
            menuMusic.stop();
            transferData.setFilename("Lindsey Stirling - Senbonzakura (MrSergio) [Harby's NM].osu");
            controller.changeScenes(SceneEnums.RHY_GAME, transferData);
        });
        songSelect8.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("Jin ft. MARiA from GARNiDELiA - daze (short ver.) (Takane6) [NM].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect9.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("Hibiki Sakura (CV Fairouz Ai) & Naruzo Machio (CV Ishikawa Kaito) - Onegai Muscle (TV Size) (Syadow-) [Easy].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect10.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("Soleily - Renatus (ExPew) [Normal].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect11.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("Soleily - Renatus (Tidek) [Hard].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect12.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("TK from Ling tosite sigure - unravel (TV edit) (Desperate-kun) [Marirose's Normal].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect13.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("BlackYooh vs. siromaru - BLACK or WHITE (DE-CADE) [NOVICE Lv.9].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect14.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("C-Show - Invitation from Mr.C (_FrEsH_ChICkEn_) [NOVICE].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect15.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("DJ OKAWARI - Flower Dance (Narcissu) [CS' Normal].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect16.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("Hibiki Sakura (CV Fairouz Ai) & Naruzo Machio (CV Ishikawa Kaito) - Onegai Muscle (TV Size) (Syadow-) [Normal].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect17.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("KANA-BOON - Silhouette (ExKagii-) [NM].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect18.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("MAKOOTO - Tanukichi no Bouken (Xinely) [Leni's Normal].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect19.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("Kairiki bear feat. GUMI, Kagamine Rin - Inai Inai Izonshou (juankristal) [timing hell].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect20.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("BlackYooh vs. siromaru - BLACK or WHITE (DE-CADE) [ADVANCED Lv.14].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect21.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("Porter Robinson & Madeon - Shelter (Dellvangel) [Loneliness].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect22.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("Soleily - Renatus (Tidek) [Insane].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect23.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("LeaF - I (Tidek) [Limbo].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });
        songSelect24.setOnAction(event->{
            menuMusic.stop();
           transferData.setFilename("Soulja Baka - Soulja Baka (jackylam5) [SMITH RMX].osu");
            controller.changeScenes(SceneEnums.RHY_GAME,transferData);
        });

        //adding all buttons to grid
        grid.add(songSelect1, 0,0,1,1);
        grid.add(songSelect2, 1,0,1,1);
        grid.add(songSelect3, 2,0,1,1);
        grid.add(songSelect4, 3,0,1,1);
        grid.add(songSelect5, 0,1,1,1);
        grid.add(songSelect6, 1,1,1,1);
        grid.add(songSelect7, 2,1,1,1);
        grid.add(songSelect8, 3,1,1,1);
        grid.add(songSelect9, 0,2,1,1);
        grid.add(songSelect10, 1,2,1,1);
        grid.add(songSelect11, 2,2,1,1);
        grid.add(songSelect12, 3,2,1,1);
        grid.add(songSelect13, 0,3,1,1);
        grid.add(songSelect14, 1,3,1,1);
        grid.add(songSelect15, 2,3,1,1);
        grid.add(songSelect16, 3,3,1,1);
        grid.add(songSelect17, 0,4,1,1);
        grid.add(songSelect18, 1,4,1,1);
        grid.add(songSelect19, 2,4,1,1);
        grid.add(songSelect20, 3,4,1,1);
        grid.add(songSelect21, 0,5,1,1);
        grid.add(songSelect22, 1,5,1,1);
        grid.add(songSelect23, 2,5,1,1);
        grid.add(songSelect24, 3,5,1,1);

        //placement adjustments
        grid.setVgap(10*heightAdjust);
        grid.setHgap(10*widthAdjust);
        grid.setTranslateX(30*widthAdjust);
        grid.setTranslateY(200*heightAdjust);

        //setting up group and scene
        Group main = new Group();
        main.getChildren().addAll(backgroundImage,grid, addTopAnchorPane(SceneEnums.GAME_SELECT));
        return new Scene(main);
    }
}
