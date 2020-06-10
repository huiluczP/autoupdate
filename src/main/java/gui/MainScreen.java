package gui;

import bean.DownloadInfo;
import net.miginfocom.swing.MigLayout;
import version.Update;
import version.VersionInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainScreen extends JFrame {

    private VersionInfo versionInfo;
    private String latestVersion = null;
    private String currentVersion = null;

    private JFrame frame = this;

    private JPanel mainPanel;
    private JPanel versionPanel;
    private JPanel processPanel;
    private JPanel buttonPanel;

    private JLabel versionLabel;
    private JLabel processLabel;
    private JProgressBar uploadProcess;

    private JButton checkButton;
    private JButton updateButton;

    public MainScreen(){
        versionInfo = new VersionInfo();
        initComponent();
    }

    private void initComponent(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new MigLayout("",
                "10px[grow]10px",
                "5px[grow]5px[grow]5px[grow]5px"));
        this.add(mainPanel, BorderLayout.CENTER);

        versionPanel = new JPanel();
        versionLabel = new JLabel();
        currentVersion = versionInfo.getCurrentVersion();
        versionLabel.setText("current version:" + currentVersion);
        versionPanel.add(versionLabel, BorderLayout.CENTER);
        mainPanel.add(versionPanel, "cell 0 0");

        processPanel = new JPanel();
        processPanel.setLayout(new MigLayout("",
                "10px[grow]10px",
                "[grow]5px[grow]"));
        uploadProcess = new JProgressBar();
        uploadProcess.setStringPainted(true);
        uploadProcess.setValue(0);
        processLabel = new JLabel("");
        processPanel.add(processLabel, "cell 0 0");
        processPanel.add(uploadProcess, "cell 0 1");
        mainPanel.add(processPanel, "cell 0 1");

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new MigLayout("",
                "10px[grow]10px[grow]10px",
                "[grow]5px"));
        checkButton = new JButton("check version");
        updateButton = new JButton("download update");
        checkButton.addActionListener(new VersionGetListener());
        updateButton.addActionListener(new UploadListener());
        buttonPanel.add(checkButton, "cell 0 0");
        buttonPanel.add(updateButton, "cell 1 0");
        mainPanel.add(buttonPanel, "cell 0 2");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setPreferredSize(new Dimension(300,400));
        this.pack();
        this.setVisible(true);
    }

    // 获取最新version
    private class VersionGetSwingWorker extends SwingWorker<String, Void>{

        @Override
        protected String doInBackground() throws Exception {
            versionInfo.getVersionInfo();
            return versionInfo.getLatestVersion();
        }

        @Override
        protected void done() {
            try {
                latestVersion = get();
                if(latestVersion!=null){
                    versionLabel.setText("<html> current version:" + currentVersion + "<br/>" +
                            "latest version:" + latestVersion + "</html>");
                }else{
                    versionLabel.setText("<html> current version:" + currentVersion + "<br/>" +
                            "can't check latest version</html>");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
    }

    // 设置版本查询按钮监听方法
    private class VersionGetListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            versionLabel.setText("<html>current version:" + currentVersion + "<br/>" +
                    "checking latest version......</html>");
            new VersionGetSwingWorker().execute();
        }
    }

    // 下载最新版本对应文件
    private class updateSwingWorker extends SwingWorker<String, Void>{

        @Override
        protected String doInBackground() throws Exception {
            String result = null;
            if(latestVersion == null){
                processLabel.setText("please check latest version first");
            }else{
                processLabel.setText("Downloading...");
                ArrayList<DownloadInfo> infos = versionInfo.getDownLoadUrl();
                result = Update.download(infos, uploadProcess);
            }
            return result;
        }

        @Override
        protected void done() {
            String result = null;
            try {
                result = get();
                if(result!=null && !result.equals("")){
                    processLabel.setText("Download Success:" + result);
                    Runtime.getRuntime().exec("cmd /k start .\\update.bat");
                    close();
                }else if(result!=null){
                    processLabel.setText("Download failed");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 设置下载更新按钮监听器
    private class UploadListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            new updateSwingWorker().execute();
        }
    }

    // 关闭主程
    private void close(){
        frame.dispose();
    }
}
