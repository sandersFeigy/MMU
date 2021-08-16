package main.java.com.hit.view;

import main.java.com.hit.client.CacheUnitClientObserver;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class implements GUI of the MMU.
 */
public class CacheUnitView extends java.lang.Object {
    private JFrame frame;
    private JLabel label;
    private PropertyChangeSupport property;
    private LoadRequest requestButton;
    private ShowStatistics showStatisticsButton;
    private String totalRequest;
    private String capacity;
    private String algorithm;
    private String totalDataModels;
    private String swapsDataModels;

    public CacheUnitView() {
        frame = new JFrame("CacheUnitUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        property = new PropertyChangeSupport(this);
        requestButton = new LoadRequest();
        showStatisticsButton = new ShowStatistics();
        label= new JLabel();
    }

    public void addPropertyChangeListener(java.beans.PropertyChangeListener pcl) {
        property.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(java.beans.PropertyChangeListener pcl) {
        property.removePropertyChangeListener(pcl);
    }

    public void start() {
        frame.setSize(500, 300);
        requestButton.setOpaque(true);
        showStatisticsButton.setOpaque(true);
        frame.setBackground(Color.BLACK);
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        panel.setBackground(Color.black);
        panel2.setPreferredSize(new Dimension(400, 150));
        panel2.setBackground(Color.LIGHT_GRAY);
        panel.add(requestButton, BorderLayout.LINE_START);
        panel.add(showStatisticsButton, BorderLayout.LINE_END);
        panel2.add(label,BorderLayout.CENTER);
        panel.add(panel2,BorderLayout.AFTER_LAST_LINE);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    public void setTotalRequest(String totalRequest) {
        this.totalRequest = totalRequest;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setTotalDataModels(String totalDataModels) {
        this.totalDataModels = totalDataModels;
    }

    public void setSwapsDataModels(String swapsDataModels) {
        this.swapsDataModels = swapsDataModels;
    }

    public <T> void updateUIData(T t) {
        label.setText((String) t);
    }

    /**
     * This class implements button to load requests to the server.
     */
    public class LoadRequest extends JPanel implements ActionListener {
        protected JButton button;
        private JFileChooser chooser;
        private String req;

        public LoadRequest() {
            super(new FlowLayout());
            button = new JButton("Load a request 📂");
            button.setPreferredSize(new Dimension(188, 40));
            button.setFocusPainted(false);
            button.setBorder(null);
            button.setVerticalTextPosition(AbstractButton.CENTER);
            button.setHorizontalTextPosition(AbstractButton.LEADING);
            add(button);
            button.addActionListener(this);
            chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "JSON");
            chooser.setFileFilter(filter);
            File f = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(f);
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                byte[] data = new byte[(int) file.length()];
                try {
                    fis.read(data);
                    fis.close();
                    req = new String(data, "UTF-8");
                    property.firePropertyChange("command", null, req);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    /**
     * This class implements button to show the statistic of the server.
     */
    public class ShowStatistics extends JPanel implements ActionListener {
        protected JButton button;

        public ShowStatistics() {
            super(new FlowLayout());
            button = new JButton("Show statistics 📊");
            button.setPreferredSize(new Dimension(188, 40));
            button.setFocusPainted(false);
            button.setBorder(null);
            button.setVerticalTextPosition(AbstractButton.CENTER);
            button.setHorizontalTextPosition(AbstractButton.LEADING);
            add(button);
            button.addActionListener(this);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String info = "<html>Capacity: " + capacity
                    + "<br>Algorithm: " + algorithm
                    + "<br>Total number of requests: " + totalRequest
                    + "<br>Total number of DataModels (GET/DELETE/U[DATE requests): " + totalDataModels
                    + "<br>Total number of DataModel swaps (from Cache to Disk):" + swapsDataModels + "</html>";
            if (totalRequest==null){
                info="No server requests have been received yet";
            }
            updateUIData(info);
        }
    }
}
