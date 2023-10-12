package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameSelectionFrame extends Frame {
    private CheckboxGroup checkboxGroup;
    private String[] player1 = new String[5];
    private String[] player2 = new String[5];
    private boolean[] player1_select = new boolean[5];
    private boolean[] player2_select = new boolean[5];
//    一轮中第几次投掷
    private int record = 0;
//    轮数
    private int total = 0;
//    总的轮数
    private int rounds = 0;
    private int player1Chips = 0;
    private int player2Chips = 0;
//  赔率
    private int player1Rate = 1;
    private int player2Rate = 1;

    //    记录是玩家1还是玩家2进行投掷
    boolean flag = true;
    public GameSelectionFrame() {
        checkboxGroup = new CheckboxGroup();

        Checkbox multiplayerCheckbox = new Checkbox("双人对战", checkboxGroup, true);
        multiplayerCheckbox.setBounds(150, 100, 150, 30);

        Checkbox singleplayerCheckbox = new Checkbox("人机对战", checkboxGroup, false);
        singleplayerCheckbox.setBounds(150, 150, 150, 30);

        Button startButton = new Button("开始游戏");
        startButton.setBounds(130, 200, 150, 30);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                init();
                // 根据用户选择执行相应的操作
                if (multiplayerCheckbox.getState()) {
//                    showGamePage();
                    showGameSettingsDialog("双人对战");
                    System.out.println("选择了双人对战");
                    // 在这里添加启动双人对战的代码
                } else if (singleplayerCheckbox.getState()) {
                    showGameSettingsDialog("人机对战");
                    System.out.println("选择了人机对战");
                    // 在这里添加启动人机对战的代码
                }
            }
        });

        setLayout(null);
        add(multiplayerCheckbox);
        add(singleplayerCheckbox);
        add(startButton);

        setTitle("游戏模式选择界面");
        setSize(400, 300);
        setVisible(true);
        centerWindow(this);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }
    //    双人对战游戏设置
    private void showGameSettingsDialog(String gameType) {
        Frame settingsFrame = new Frame("游戏设置");
        settingsFrame.setSize(300, 200);
        settingsFrame.setLayout(new GridLayout(0, 1));

        Panel roundsPanel = new Panel(new FlowLayout());
        Label roundsLabel = new Label("游戏局数:");
        TextField roundsTextField = new TextField(10);

        Panel player1Panel = new Panel(new FlowLayout());
        Label player1ChipsLabel = new Label("玩家1筹码数:");
        TextField player1ChipsTextField = new TextField(10);

        Panel player2Panel = new Panel(new FlowLayout());
        Label player2ChipsLabel = new Label("玩家2筹码数:");
        if("人机对战".equals(gameType)){
            player2ChipsLabel.setText("人机筹码数:");
        }
        TextField player2ChipsTextField = new TextField(10);

        Panel startGamePanel = new Panel(new FlowLayout());
        Button startGameButton = new Button("开始游戏");
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取设置的参数，然后在这里启动游戏
                try{
                    int r = Integer.parseInt(roundsTextField.getText());
                    int player1Chip = Integer.parseInt(player1ChipsTextField.getText());
                    int player2Chip= Integer.parseInt(player2ChipsTextField.getText());
                    rounds = r;
                    player1Chips = player1Chip;
                    player2Chips = player2Chip;
                    // 在这里添加启动游戏的代码，传递参数 rounds、player1Chips 和 player2Chips
                    System.out.println("开始游戏，游戏类型：" + gameType +
                            "，游戏局数：" + rounds +
                            "，玩家1筹码数：" + player1Chips +
                            "，玩家2筹码数：" + player2Chips);
                    showGamePage(gameType);
                    // 关闭设置窗口
                    settingsFrame.dispose();

                }catch (Exception exception){
                    showWarningDialog("只能输入整数！");
                }
            }
        });
        centerWindow(settingsFrame);
        roundsPanel.add(roundsLabel);
        roundsPanel.add(roundsTextField);
        settingsFrame.add(roundsPanel);
        player1Panel.add(player1ChipsLabel);
        player1Panel.add(player1ChipsTextField);
        settingsFrame.add(player1Panel);
        player2Panel.add(player2ChipsLabel);
        player2Panel.add(player2ChipsTextField);
        settingsFrame.add(player2Panel);
        startGamePanel.add(startGameButton);
        settingsFrame.add(startGamePanel);
        settingsFrame.setVisible(true);

        settingsFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

//      设置页面对错误输入对提示
    private void showWarningDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "警告", JOptionPane.WARNING_MESSAGE);
    }

//    窗口居中设置
    private void centerWindow(Frame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();

        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;

        frame.setLocation(x, y);
    }

    private void init(){
        for(int i=0;i<5;i++){
            player1[i]=" ";
            player2[i]=" ";
            player1_select[i]=false;
            player2_select[i]=false;
        }
    }

    //    游戏界面
    private void showGamePage(String gameType) {
        Frame gameFrame;
        // 创建新的游戏窗口
        gameFrame = new Frame("骰子游戏");
//        gameFrame.setLayout(new BorderLayout());
        gameFrame.setLayout(new GridLayout(0, 1));
        // 初始化四个部分
        Panel throwArea = new Panel();
        Panel selectionArea = new Panel();
        Panel scoreArea = new Panel();
        Panel throwButtonArea = new Panel();


        // 第一个部分：投掷区域


        throwArea.setLayout(new FlowLayout());
        Label currentLabel = new Label("投掷区域：");
        throwArea.add(currentLabel);
        for (int i = 1; i <= 5; i++) {
            throwArea.add(new Label("   " + i + "   "));
            // 这里可以添加显示图片的组件，例如 Image 或者 ImageIcon
        }
//        Panel imgPanel = new Panel(new BorderLayout());
//        imgPanel.add(canvas, BorderLayout.CENTER);
//        throwArea.add(imgPanel);

        // 第二个部分：选定区域
        selectionArea.setLayout(new FlowLayout());
        Label selectLabel = new Label("选定区域：");
        selectionArea.add(selectLabel);
//        Checkbox checkbox = new Checkbox();
        Checkbox checkbox1 = new Checkbox();
        Checkbox checkbox2 = new Checkbox();
        Checkbox checkbox3 = new Checkbox();
        Checkbox checkbox4 = new Checkbox();
        Checkbox checkbox5 = new Checkbox();
        checkbox1.setEnabled(false);
        checkbox2.setEnabled(false);
        checkbox3.setEnabled(false);
        checkbox4.setEnabled(false);
        checkbox5.setEnabled(false);
        selectionArea.add(checkbox1);
        selectionArea.add(checkbox2);
        selectionArea.add(checkbox3);
        selectionArea.add(checkbox4);
        selectionArea.add(checkbox5);


        // 第三个部分：得分区域
        scoreArea.setLayout(new GridLayout(0, 1));
        Panel s1 = new Panel();
        Panel s2 = new Panel();
        s1.setLayout(new FlowLayout());
        Label player1Text = new Label("玩家1牌型：");
        s1.add(player1Text);
        for (int i = 0; i < 5; i++) {
            s1.add(new Label("   " + player1[i] + "   "));
            // 这里可以添加显示图片的组件，例如 Image 或者 ImageIcon
        }

        s2.setLayout(new FlowLayout());
        Label player2Text = new Label("玩家2牌型：");
        if("人机对战".equals(gameType)){
            player2Text.setText("人机牌型：");
        }
        s2.add(player2Text);
        for (int i = 0; i < 5; i++) {
            s2.add(new Label("   " + player2[i] + "   "));
            // 这里可以添加显示图片的组件，例如 Image 或者 ImageIcon
        }
        scoreArea.add(s1);
        scoreArea.add(s2);

        // 第四个部分：投掷按钮
        Button throwButton = new Button("玩家1 第一次投掷");
        throwButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                selectionArea.removeAll();
                throwArea.removeAll();
                s1.removeAll();
                s2.removeAll();
                throwArea.add(currentLabel);
                s1.add(player1Text);
                s2.add(player2Text);
//                获取锁定
                if(record>0) {
                    if("人机对战".equals(gameType)&&record%2==0){
                        if (checkbox1.getState() == true) {
                            player1_select[0] = true;
                        }
                        if (checkbox5.getState() == true) {
                            player1_select[4] = true;
                        }
                        if (checkbox2.getState() == true) {
                          player1_select[1] = true;

                        }
                        if (checkbox3.getState() == true) {
                           player1_select[2] = true;

                        }
                        if (checkbox4.getState() == true) {
                           player1_select[3] = true;
                        }
                    }else{
                        if (checkbox1.getState() == true) {
                            if (flag == true) {
                                player2_select[0] = true;
                            } else {
                                player1_select[0] = true;
                            }
                        }
                        if (checkbox5.getState() == true) {
                            if (flag == true) {
                                player2_select[4] = true;
                            } else {
                                player1_select[4] = true;
                            }
                        }
                        if (checkbox2.getState() == true) {
                            if (flag == true) {
                                player2_select[1] = true;
                            } else {
                                player1_select[1] = true;
                            }
                        }
                        if (checkbox3.getState() == true) {
                            if (flag == true) {
                                player2_select[2] = true;
                            } else {
                                player1_select[2] = true;
                            }
                        }
                        if (checkbox4.getState() == true) {
                            if (flag == true) {
                                player2_select[3] = true;
                            } else {
                                player1_select[3] = true;
                            }
                        }
                    }
                }
//                修改牌面的值
                for (int i = 0; i < 5; i++) {
                    Random random = new Random();
                    // 生成一个1到5闭区间的随机整数
                    int randomNumber = 1 + random.nextInt(5);
                    if(flag==true) {
                        if(player1_select[i]==false){
                            player1[i] = "" + randomNumber;
                        }
                    }else{
                        if(player2_select[i]==false){
                            player2[i] = "" + randomNumber;
                        }
                    }
                    s1.add(new Label("   " + player1[i] + "   "));
                    s2.add(new Label("   " + player2[i] + "   "));
                    throwArea.add(new Label("   " + randomNumber + "   "));
                    scoreArea.add(s1);
                    scoreArea.add(s2);
                }
                checkbox1.setEnabled(true);

                checkbox2.setEnabled(true);

                checkbox3.setEnabled(true);

                checkbox4.setEnabled(true);

                checkbox5.setEnabled(true);
//                设置不可选
                if(flag==true){
                    if(player1_select[0]==true){
                        checkbox1.setEnabled(false);
                        checkbox1.setState(false);
                    }
                    if(player1_select[1]==true){
                        checkbox2.setEnabled(false);
                        checkbox2.setState(false);
                    }
                    if(player1_select[2]==true){
                        checkbox3.setEnabled(false);
                        checkbox3.setState(false);
                    }
                    if(player1_select[3]==true){
                        checkbox4.setEnabled(false);
                        checkbox4.setState(false);
                    }
                    if(player1_select[4]==true){
                        checkbox5.setEnabled(false);
                        checkbox5.setState(false);
                    }
                }
                else{
                    if(player2_select[0]==true){
                        checkbox1.setEnabled(false);
                        checkbox1.setState(false);
                    }
                    if(player2_select[1]==true){
                        checkbox2.setEnabled(false);
                        checkbox2.setState(false);
                    }
                    if(player2_select[2]==true){
                        checkbox3.setEnabled(false);
                        checkbox3.setState(false);
                    }
                    if(player2_select[3]==true){
                        checkbox4.setEnabled(false);
                        checkbox4.setState(false);
                    }
                    if(player2_select[4]==true){
                        checkbox5.setEnabled(false);
                        checkbox5.setState(false);
                    }
                }
//                for(int i=0;i<5;i++){
//                    System.out.println(player1_select[i] + " " + player2_select[i]);
//                }
//                System.out.println("=============");
                flag = !flag;
                checkbox1.setState(false);
                checkbox2.setState(false);
                checkbox3.setState(false);
                checkbox4.setState(false);
                checkbox5.setState(false);
                selectionArea.add(selectLabel);
                selectionArea.add(checkbox1);
                selectionArea.add(checkbox2);
                selectionArea.add(checkbox3);
                selectionArea.add(checkbox4);
                selectionArea.add(checkbox5);

                String play = "玩家";
                if (flag){
                    play+="1 ";
                }else {
                    play+="2 ";
                    if("人机对战".equals(gameType)){
                        play = "人机";
                    }
                }
                record = record + 1;
                int lun = record/2;
                if (lun==0){
                    play+="第一次投掷";
                }else if (lun==1){
                    play+="第二次投掷";
                }else if(lun == 2) {
                    play+="第三次投掷";
                }else{
                    play+="第一次投掷";
                }
//                只能选两次
                if (record>=5){
                    selectionArea.removeAll();
                    checkbox1.setEnabled(false);
                    checkbox2.setEnabled(false);
                    checkbox3.setEnabled(false);
                    checkbox4.setEnabled(false);
                    checkbox5.setEnabled(false);
                    selectionArea.add(selectLabel);
                    selectionArea.add(checkbox1);
                    selectionArea.add(checkbox2);
                    selectionArea.add(checkbox3);
                    selectionArea.add(checkbox4);
                    selectionArea.add(checkbox5);
                }
//                一轮结束
                if (record==6){
                    total = total + 1;
                    showDialog(gameFrame,gameType);
                    record = 0;
                    init();
                    s1.removeAll();
                    s2.removeAll();
                    s1.add(player1Text);
                    s2.add(player2Text);
                    scoreArea.add(s1);
                    scoreArea.add(s2);
                }

                throwButton.setLabel(play);
                gameFrame.validate();
//                设置倍率
                if (record==5){
                    showDialogWithChoice(gameFrame,"1");
                    if("人机对战".equals(gameType)){
                        Random random = new Random();
                        // 生成随机数，范围为0到2（包括0和2）
                        int randomNumber = random.nextInt(3);
                        player2Rate =player2Rate+ randomNumber;
                    }else{
                        showDialogWithChoice(gameFrame,"2");
                    }

                }
//                奇数是人机或者玩家二
                if(record%2==1&&"人机对战".equals(gameType)){

                    try {
                        // 设置线程休眠时间为3秒（3000毫秒）
                        Thread.sleep(1000);
                    } catch (InterruptedException exception) {
                        // 处理中断异常（如果有）
                        exception.printStackTrace();
                    }
                    // 再次触发按钮点击事件
                    ActionEvent event = new ActionEvent(throwButton, ActionEvent.ACTION_PERFORMED, throwButton.getActionCommand());
                    for (ActionListener listener : throwButton.getActionListeners()) {
                        listener.actionPerformed(event);
                    }
//                    //  随机给人机设置锁定的值
//                    if("人机对战".equals(gameType)){
//                        Random random = new Random();
//                        // 生成随机数，范围为0到4（包括0和4）
//                        int randomNumber = random.nextInt(5);
//                        for(int i=0;i<randomNumber;i++){
//                            int t = random.nextInt(5);
//                            player2_select[t] = true;
//                            System.out.println(t);
//                        }
//                    }
                }
//            button.notifyListeners(SWT.Selection, new Event());
            }

        });
        throwButtonArea.add(throwButton);

        // 将四个部分添加到游戏窗口

        gameFrame.add(throwArea, BorderLayout.NORTH);
        gameFrame.add(selectionArea, BorderLayout.WEST);
        gameFrame.add(scoreArea, BorderLayout.NORTH);
        gameFrame.add(throwButtonArea, BorderLayout.SOUTH);

        gameFrame.setSize(600, 400);
        centerWindow(gameFrame);
        gameFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        gameFrame.setVisible(true);
    }


//    增加倍率的界面
    private void showDialogWithChoice(Frame parent,String play) {
        Dialog dialog = new Dialog(parent, "倍率选择", true); // modal dialog
        dialog.setLayout(new FlowLayout());
        dialog.setSize(250, 150);

        Choice choice = new Choice();
        choice.add("0");
        choice.add("1");
        choice.add("2");
        dialog.add(new Label("请玩家" + play + "选择要增加的倍率："));
        dialog.add(choice);

        Button closeButton = new Button("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedValue = choice.getSelectedItem();
                if ("1".equals(play)){
                    player1Rate = Integer.parseInt(player1Rate + selectedValue);
                }else {
                    player2Rate = Integer.parseInt(player2Rate + selectedValue);
                }
//                System.out.println(selectedValue);
                dialog.setVisible(false);
            }
        });
        dialog.add(closeButton);

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private Integer getScore(String[] dian){
        int[] array = new int[dian.length];
        int value = 0;
        // 遍历stringArray并将每个String解析为int并存储在intArray中
        for (int i = 0; i < dian.length; i++) {
            array[i] = Integer.parseInt(dian[i]);
        }
//        双对
        int found = 0;
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] == array[j]) {
                    found ++;
                    break; // 找到一组相同的数字就可以退出内层循环
                }
            }
        }
        if (found==2) {
            value  = 10;
        }
//        三连
        boolean found1 = false;

        for (int i = 0; i < array.length - 2; i++) {
            for (int j = i + 1; j < array.length - 1; j++) {
                for (int k = j + 1; k < array.length; k++) {
                    if (array[i] == array[j] && array[i] == array[k]) {
                        found1 = true;
                        break;
                    }
                }
                if (found1) {
                    break;
                }
            }
            if (found1) {
                break;
            }
        }
        if (found1) {
            value = 10;
        }
//        葫芦
        // 创建一个数组用于记录数字出现的次数
        int[] countArray = new int[101]; // 假设数字的范围在0到100之间
        boolean foundThreeEqual = false;
        boolean foundTwoEqual = false;
        // 统计每个数字的出现次数
        for (int num : array) {
            countArray[num]++;
        }

        for (int i = 0; i < countArray.length; i++) {
            if (countArray[i] == 3) {
                foundThreeEqual = true;
            } else if (countArray[i] == 2) {
                foundTwoEqual = true;
            }
        }

        if (foundThreeEqual && foundTwoEqual) {
            value = 20;
        }
//        小顺子
        boolean foundFourConsecutive = false;

        // 首先对数组进行排序
        Arrays.sort(array);

        for (int i = 0; i < array.length - 3; i++) {
            if (array[i + 1] - array[i] == 1 &&
                    array[i + 2] - array[i + 1] == 1 &&
                    array[i + 3] - array[i + 2] == 1) {
                foundFourConsecutive = true;
                break; // 找到满足条件的四个连续值后就可以退出循环
            }
        }

        if (foundFourConsecutive) {
            value = 30;
        }

//        四连
        boolean foundFourEqual = false;

        // 创建一个数组用于记录数字出现的次数
        int[] countArray1 = new int[101]; // 假设数字的范围在0到100之间

        // 统计每个数字的出现次数
        for (int num : array) {
            countArray1[num]++;
        }

        for (int i = 0; i < countArray1.length; i++) {
            if (countArray1[i] == 4) {
                foundFourEqual = true;
                break; // 找到相等的4个值后就可以退出循环
            }
        }

        if (foundFourEqual) {
            value = 40;
        }
//        大顺子
        boolean foundFiveConsecutive = false;

        // 将数组元素放入一个集合
        Set<Integer> set = new HashSet<>();
        for (int num : array) {
            set.add(num);
        }

        // 检查集合的大小是否为5，并检查最大值和最小值之间的差是否为4
        if (set.size() == 5) {
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;
            for (int num : set) {
                max = Math.max(max, num);
                min = Math.min(min, num);
            }
            if (max - min == 4) {
                foundFiveConsecutive = true;
            }
        }

        if (foundFiveConsecutive) {
            value = 60;
        }
//        五连
        boolean allEqual = true;

        for (int i = 1; i < array.length; i++) {
            if (array[i] != array[0]) {
                allEqual = false;
                break; // 一旦发现有不相等的元素就可以退出循环
            }
        }

        if (allEqual) {
           value = 100;
        }

        return value;
    }

//    一轮结束的页面
    private void showDialog(Frame parent,String gameType) {
        Dialog dialog = new Dialog(parent, "提示", true); // true makes it modal
//        dialog.setLayout(new FlowLayout());
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(0, 1));
        // 初始化四个部分
        Panel scoreArea = new Panel();
        scoreArea.setLayout(new GridLayout(0, 1));
        Panel s1 = new Panel();
        Panel s2 = new Panel();
        s1.setLayout(new FlowLayout());
        Label player1Text = new Label("玩家1牌型：");
        s1.add(player1Text);
        for (int i = 0; i < 5; i++) {
            s1.add(new Label("   " + player1[i] + "   "));
            // 这里可以添加显示图片的组件，例如 Image 或者 ImageIcon
        }

        s2.setLayout(new FlowLayout());
        Label player2Text = new Label("玩家2牌型：");
        s2.add(player2Text);
        if("人机对战".equals(gameType)){
            player2Text.setText("人机牌型：");
        }
        for (int i = 0; i < 5; i++) {
            s2.add(new Label("   " + player2[i] + "   "));
            // 这里可以添加显示图片的组件，例如 Image 或者 ImageIcon
        }
        scoreArea.add(s1);
        scoreArea.add(s2);
        dialog.add(scoreArea);
        Panel win = new Panel();
        Integer score1 = getScore(player1);
        Integer score2 = getScore(player2);
        Label score = new Label();
        if(score1>score2){
            score.setText("玩家1胜，赢了玩家2  " + (score1-score2) + "点筹码");
        }else if(score1<score2){
            score.setText("玩家2胜，赢了玩家1  " + (score1-score2) + "点筹码");
            if("人机对战".equals(gameType)){
                score.setText("人机胜，赢了玩家1  " + (score1-score2) + "点筹码");
            }
        }else {
            score.setText("玩家和玩家2平局");
            if("人机对战".equals(gameType)){
                score.setText("玩家和人机平局");
            }
        }
        win.add(score);
        dialog.add(win);
        Panel chip1 = new Panel();
        Panel chip2 = new Panel();
        if(score1>score2){
            player1Chips = player1Chips +  (score1-score2) ;
            player2Chips = player2Chips -  (score1-score2) ;
        }else if(score1<score2){
            player2Chips = player2Chips +  (score2-score1) ;
            player1Chips = player1Chips -  (score2-score1) ;
        }
        Label t1 = new Label("玩家1的筹码数为：" + player1Chips);
        Label t2 = new Label("玩家2的筹码数为：" + player2Chips);
        if("人机对战".equals(gameType)){
            t2.setText("人机的筹码数为：" + player2Chips);
        }
        chip1.add(t1);
        chip2.add(t2);
        dialog.add(chip1);
        dialog.add(chip2);


        Button closeButton = new Button();
        if(total<rounds){
            closeButton.setLabel("下一轮");
        }else {
            closeButton.setLabel("结束游戏");
        }
        if(player2Chips<=0){
            closeButton.setLabel("结束游戏");
        }
        if(player1Chips<=0){
            closeButton.setLabel("结束游戏");
        }
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(total>=rounds||player2Chips<=0||player1Chips<=0){
                    System.exit(0);
                }else {
                    dialog.setVisible(false);
                }

            }
        });
        dialog.add(closeButton);

        dialog.setLocationRelativeTo(parent); // center the dialog on parent window
        dialog.setVisible(true);
    }
    public static void main(String[] args) {
        new GameSelectionFrame();
    }

}
