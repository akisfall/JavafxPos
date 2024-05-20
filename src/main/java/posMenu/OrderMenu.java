package posMenu;
import java.util.TreeMap;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import models.OrderDetail;
import models.Product;
import models.ReadCategoryProduct;

public class OrderMenu extends Application {

    private TilePane menuJuice = getProductCategoryMenu("水果");
    TilePane menuTea = getProductCategoryMenu("單茶");
    TilePane menuCoffee = getProductCategoryMenu("奶茶");
    TilePane menuLifeusage = getProductCategoryMenu("日用品");
    private ObservableList<OrderDetail> order_list;
    private TableView<OrderDetail> table;
    private final TreeMap<String, Product> product_dict = ReadCategoryProduct.readProduct();
    private final TextArea display = new TextArea();

    public TilePane getProductCategoryMenu(String category) {
        TreeMap<String, Product> product_dict = ReadCategoryProduct.readProduct();
        TilePane category_menu = new TilePane();
        category_menu.setVgap(10);
        category_menu.setHgap(10);
        category_menu.setPrefColumns(4);
        for (String item_id : product_dict.keySet()) {
            if (product_dict.get(item_id).getCategory().equals(category)) {
                Button btn = new Button();
                btn.setPrefSize(120, 120);
                Image img = new Image(this.getClass().getResourceAsStream("/pos/javafxpos/imgs/"+product_dict.get(item_id).getPhoto()));
                ImageView imgview = new ImageView(img);
                imgview.setFitHeight(80);
                imgview.setPreserveRatio(true);
                btn.setGraphic(imgview);
                category_menu.getChildren().add(btn);
                Tooltip productName = new Tooltip(product_dict.get(item_id).getDescription());
                productName.setStyle("-fx-font:18 calibre");
                productName.setShowDelay(Duration.seconds(0));
                btn.setTooltip(productName);
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        addToCart(item_id);
                    }
                });
            }
        }
        return category_menu;
    }
    VBox menuContainerPane = new VBox();
    public TilePane getMenuSelectionContainer() {

        Button btnJuice = new Button("水果");
        btnJuice.setStyle("-fx-font:14 calibre");
        btnJuice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                menuContainerPane.getChildren().clear();
                menuContainerPane.getChildren().add(menuJuice);
            }
        });
        Button btnTea = new Button("單茶");
        btnTea.setStyle("-fx-font:14 calibre");
        btnTea.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                select_category_menu(e);
            }
        });
        Button btnMilkTea = new Button("奶茶");
        btnMilkTea.setStyle("-fx-font:14 calibre");
        btnMilkTea.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                select_category_menu(e);
            }
        });
        Button btnDailyUsage = new Button("日用品");
        btnDailyUsage.setStyle("-fx-font:14 calibre");
        btnDailyUsage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                select_category_menu(event);
            }
        });
        TilePane containerCategoryMenuBtn = new TilePane();
        containerCategoryMenuBtn.setVgap(10);
        containerCategoryMenuBtn.setHgap(10);

        containerCategoryMenuBtn.getChildren().add(btnJuice);
        containerCategoryMenuBtn.getChildren().add(btnTea);
        containerCategoryMenuBtn.getChildren().add(btnMilkTea);
        containerCategoryMenuBtn.getChildren().add(btnDailyUsage);
        return containerCategoryMenuBtn;
    }
    public void select_category_menu(ActionEvent event) {
        String category = ((Button) event.getSource()).getText();
        menuContainerPane.getChildren().clear();
        switch (category) {
            case "水果":
                menuContainerPane.getChildren().add(menuJuice);
                break;
            case "單茶":
                menuContainerPane.getChildren().add(menuTea);
                break;
            case "奶茶":
                menuContainerPane.getChildren().add(menuCoffee);
                break;
            case "日用品":
                menuContainerPane.getChildren().add(menuLifeusage);
            default:
                break;
        }

    }
    public TilePane getOrderOperationContainer() {
        Button btnAdd = new Button("感覺手氣好嗎？");
        btnAdd.setStyle("-fx-font:14 calibre;");
        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                int r = (int)(Math.random()*(119-101+1)+101);
                addToCart("p"+r);
                display.appendText("你抽到了："+product_dict.get("p"+r).getName());
            }
        });

        Button btnDelete = new Button("刪除一筆");
        btnDelete.setStyle("-fx-font:14 calibre;");
        btnDelete.setOnAction((ActionEvent e) -> {
            OrderDetail selectedItem = table.getSelectionModel().getSelectedItem();
            if(order_list.isEmpty()){ return;}
            if(selectedItem == null ){
                order_list.remove(0);
            }
            order_list.remove(selectedItem);
            checkTotal();
        });
        Button btnClear = new Button("清除清單");
        btnClear.setStyle("-fx-font:14 calibre");
        btnClear.setOnAction(actionEvent -> {
            order_list.clear();
            table.refresh();
            checkTotal();
        });
        TilePane operationBtnTile = new TilePane();
        operationBtnTile.getChildren().add(btnAdd);
        operationBtnTile.getChildren().add(btnDelete);
        operationBtnTile.getChildren().add(btnClear);
        return operationBtnTile;
    }

    public void initializeOrderTable() {
        order_list = FXCollections.observableArrayList();
        checkTotal();
        table = new TableView<>();
        table.setEditable(true);
        table.setPrefHeight(300);

        TableColumn<OrderDetail, String> order_item_name = new TableColumn<>("品名");
        order_item_name.setCellValueFactory(new PropertyValueFactory("product_name"));
        order_item_name.setPrefWidth(100);
        order_item_name.setMinWidth(100);
        TableColumn<OrderDetail, Integer> order_item_price = new TableColumn<>("價格");
        order_item_price.setCellValueFactory(new PropertyValueFactory("product_price"));
        TableColumn<OrderDetail, Integer> order_item_qty = new TableColumn<>("數量");
        order_item_qty.setCellValueFactory(new PropertyValueFactory("quantity"));
        order_item_qty.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        order_item_qty.setOnEditCommit(event -> {
            int row_num = event.getTablePosition().getRow();
            int new_val = event.getNewValue();
            OrderDetail target = event.getTableView().getItems().get(row_num);
            if (new_val <= 0) {
                new_val = 1;
                table.refresh();
            }
            target.setQuantity(new_val);
            checkTotal();
        });

        order_item_name.setReorderable(false);
        order_item_qty.setReorderable(false);
        order_item_price.setReorderable(false);

        table.setItems(order_list);
        Tooltip tableToolTip = new Tooltip("在數量上點兩下可以修改喔！");
        tableToolTip.setStyle("-fx-font:18 calibre;");
        tableToolTip.setShowDelay(Duration.seconds(1));
        table.setTooltip(tableToolTip);
        table.setStyle("-fx-font:18 calibre");
        checkTotal();
        table.getColumns().addAll(order_item_name, order_item_price, order_item_qty);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void checkTotal() {
        double total = 0;
        for (OrderDetail od : order_list) {
            total += od.getProduct_price() * od.getQuantity();
        }

        String totalmsg = String.format("%s %d\n", "總金額:", Math.round(total));
        display.setText(totalmsg);
        display.setStyle("-fx-font: 25 Calibre; -fx-text-fill:green; -fx-opacity:1;");
    }

    public void addToCart(String item_id) {
        boolean duplication = false;
        for (int i = 0; i < order_list.size(); i++) {
            if (order_list.get(i).getProduct_id().equals(item_id)) {
                int qty = order_list.get(i).getQuantity() + 1; //拿出qty並+1
                order_list.get(i).setQuantity(qty);
                duplication = true;
                table.refresh();
                checkTotal();
                break;
            }
        }
        if (!duplication) {
            OrderDetail new_ord = new OrderDetail(
                    item_id,
                    product_dict.get(item_id).getName(),
                    product_dict.get(item_id).getPrice(),
                    1);
            order_list.add(new_ord);
            checkTotal();
        }
    }
    @Override
    public void start(Stage stage) {
        HBox root = new HBox();
        VBox div1 = new VBox();
        VBox div2 = new VBox();
        root.getChildren().add(div1);
        root.getChildren().add(div2);
        root.setSpacing(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.getStylesheets().add(String.valueOf(this.getClass().getResource("/pos/javafxpos/css/bootstrap3.css")));
        TilePane menuSelectionTile = getMenuSelectionContainer();
        div1.getChildren().add(menuSelectionTile);
        menuContainerPane.getChildren().add(menuJuice);
        div1.getChildren().add(menuContainerPane);
        div2.getChildren().add(getOrderOperationContainer());
        initializeOrderTable();
        div2.getChildren().add(table);
        div2.getChildren().add(display);
        display.setPrefColumnCount(10);
        display.setDisable(true);
        Scene scene = new Scene(root,900,650);
        stage.setResizable(false);
        stage.setTitle("手搖飲與水果代購點單系統");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

