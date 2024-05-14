package tn.esprit.controllers.ASSURANCE;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import tn.esprit.services.Assurance_s;
import tn.esprit.services.Contrat_s;
import tn.esprit.entities.Assurance;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import tn.esprit.utils.DataBase;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AfficherBack {

    @FXML
    private TableView<Assurance> tableau;

    @FXML
    private TableColumn<Assurance, Number> idtv;

    @FXML
    private TableColumn<Assurance, String> nomtv;

    @FXML
    private TableColumn<Assurance, String> typetv;

    @FXML
    private TableColumn<Assurance, Number> montanttv;

    @FXML
    private TableColumn<Assurance, String> datedebuttv;

    @FXML
    private TableColumn<Assurance, String> datefintv;

    @FXML
    private TableColumn<Assurance, Void> editColumn;

    @FXML
    private TableColumn<Assurance, Assurance> deleteColumn;

    @FXML
    private TextField filterField;

    private Assurance_s assuranceService = Assurance_s.getInstance();
    private FilteredList<Assurance> filteredData;
    private Connection cnx = DataBase.getInstance().getConx();

    @FXML
    void initialize() {
        initializeTable();
        refreshAssuranceList();
        setupRowSelectListener();

        filteredData = new FilteredList<>(tableau.getItems(), p -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(assurance -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                Contrat_s contratService = new Contrat_s();
                String typeCouverture = null;
                try {
                    typeCouverture = contratService.getTypeCovertureByContratId(assurance.getContrat_id());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return assurance.getNom() != null && assurance.getNom().toLowerCase().contains(lowerCaseFilter) ||
                        (typeCouverture != null && typeCouverture.toLowerCase().contains(lowerCaseFilter)) ||
                        Float.toString(assurance.getMontant()).toLowerCase().contains(lowerCaseFilter);
            });
        });

        tableau.setItems(filteredData);
        initModifierColumn();
    }

    private void setupRowSelectListener() {
        tableau.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nomtv.setText(newValue.getNom());
                montanttv.setText(String.valueOf(newValue.getMontant()));
                datedebuttv.setText(newValue.getDate_debut().toString());
                datefintv.setText(newValue.getDate_fin().toString());

                // Mettre à jour la colonne "Type de couverture"
                int contratId = newValue.getContrat_id();
                try {
                    Contrat_s contratService = new Contrat_s();
                    String typeCouverture = contratService.getTypeCovertureByContratId(contratId);
                    typetv.setText(typeCouverture != null ? typeCouverture : "N/A");
                } catch (SQLException e) {
                    e.printStackTrace();
                    typetv.setText("N/A");
                }
            }
        });
    }



    private void refreshAssuranceList() {
        try {
            List<Assurance> assuranceList = assuranceService.show();
            tableau.setItems(FXCollections.observableArrayList(assuranceList));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeTable() {
        idtv.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomtv.setCellValueFactory(new PropertyValueFactory<>("nom"));
        montanttv.setCellValueFactory(new PropertyValueFactory<>("montant"));
        datedebuttv.setCellValueFactory(new PropertyValueFactory<>("date_debut"));
        datefintv.setCellValueFactory(new PropertyValueFactory<>("date_fin"));

        typetv.setCellValueFactory(cellData -> {
            int contratId = cellData.getValue().getContrat_id();
            try {
                Contrat_s contratService = new Contrat_s();
                String typeCouverture = contratService.getTypeCovertureByContratId(contratId);
                return new SimpleStringProperty(typeCouverture != null ? typeCouverture : "N/A");
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("N/A");
            }
        });

        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(new Callback<TableColumn<Assurance, Assurance>, TableCell<Assurance, Assurance>>() {
            @Override
            public TableCell<Assurance, Assurance> call(TableColumn<Assurance, Assurance> param) {
                return new TableCell<Assurance, Assurance>() {
                    final Button deleteButton = new Button("Delete");

                    @Override
                    protected void updateItem(Assurance assurance, boolean empty) {
                        super.updateItem(assurance, empty);

                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(deleteButton);
                            deleteButton.setOnAction(event -> {
                                try {
                                    assuranceService.delete(assurance.getId());
                                    refreshAssuranceList();
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setContentText("Assurance supprimée avec succès");
                                    alert.show();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                };
            }
        });
    }

    private void initModifierColumn() {
        editColumn.setCellFactory(column -> {
            return new TableCell<Assurance, Void>() {
                private final Button editButton = new Button("Modifier");

                {
                    editButton.setOnAction(event -> {
                        Assurance assurance = getTableView().getItems().get(getIndex());
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assurancefxml/ModifierAssurance.fxml"));
                        try {
                            Parent root = loader.load();
                            ModifierAssurance controller = loader.getController();
                            controller.initData(assurance);
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.showAndWait();
                            refreshAssuranceList();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(editButton);
                    }
                }
            };
        });
    }
    @FXML
    void ExporterExcel(ActionEvent event) {
        try {
            List<Assurance> assurances = assuranceService.show(); // Assuming assuranceService is an instance of Assurance_s
            exportAssurancesToExcel(assurances);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exportAssurancesToExcel(List<Assurance> assurances) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Assurances");
        Row headerRow = sheet.createRow(0);
        String[] columnNames = {"ID", "Nom", "Montant", "Date début", "Date fin", "Contrat ID"};
        for (int i = 0; i < columnNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnNames[i]);
        }

        int rowNum = 1;
        for (Assurance assurance : assurances) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(assurance.getId());
            row.createCell(1).setCellValue(assurance.getNom());
            row.createCell(2).setCellValue(assurance.getMontant());
            row.createCell(3).setCellValue(assurance.getDate_debut());
            row.createCell(4).setCellValue(assurance.getDate_fin());
            row.createCell(5).setCellValue(assurance.getContrat_id());
        }

        for (int i = 0; i < columnNames.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream("Assurances.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void goToAjouterAssurance(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assurancefxml/AjouterAssurance.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void goToListeContrat(ActionEvent actionEvent) {try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assurancefxml/AfficherBackcontrat.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        // Handle the error gracefully, like showing an alert or logging it
    }
    }

}
