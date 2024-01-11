package app.form.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class CalculatorForm {
    private JPanel panel;
    private JButton btn_ac;
    private JButton btn_7;
    private JButton btn_division;
    private JButton btn_moltiplication;
    private JButton btn_opbracket;
    private JButton btn_8;
    private JButton btn_clbracket;
    private JButton btn_9;
    private JButton btn_6;
    private JButton btn_subtraction;
    private JButton btn_5;
    private JButton btn_4;
    private JButton btn_1;
    private JButton btn_2;
    private JButton btn_3;
    private JButton btn_addition;
    private JButton btn_0;
    private JButton btn_comma;
    private JButton btn_rpn;
    private JButton btn_result;
    private JLabel lbl_result;
    private JLabel lbl_welcome;
    private JLabel lbl_username;
    private JButton btn_history;

    private Queue<String> expression;
    private Stack<Character> operators;

    Boolean rpn;

    public CalculatorForm(String username) {
        expression = new LinkedList<>();
        operators = new Stack<>();

        rpn = false;
        lbl_username.setText(username);

        btn_0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNumber(0);
            }
        });

        btn_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNumber(1);
            }
        });

        btn_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNumber(2);
            }
        });

        btn_3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNumber(3);
            }
        });

        btn_4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNumber(4);
            }
        });

        btn_5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNumber(5);
            }
        });

        btn_6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNumber(6);
            }
        });

        btn_7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNumber(7);
            }
        });

        btn_8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNumber(8);
            }
        });

        btn_9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddNumber(9);
            }
        });

        btn_ac.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reset();
            }
        });

        btn_opbracket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!rpn){
                    AddSign('(');
                }
                else{
                    lbl_result.setText(lbl_result.getText() + " ");
                }
            }
        });

        btn_clbracket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSign(')');
            }
        });

        btn_division.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSign('/');
            }
        });

        btn_moltiplication.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSign('×');
            }
        });

        btn_subtraction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSign('-');
            }
        });

        btn_addition.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSign('+');
            }
        });

        btn_result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Result();
            }
        });

        btn_comma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSign(',');
            }
        });

        btn_rpn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!rpn){
                    btn_rpn.setBackground(Color.green);
                    rpn = true;

                    btn_opbracket.setText("_");
                    btn_clbracket.setEnabled(false);
                }
                else{
                    btn_rpn.setBackground(new Color(102, 98, 118));
                    rpn = false;

                    btn_opbracket.setText("(");
                    btn_clbracket.setEnabled(true);
                }
                lbl_result.setText("0");
            }
        });
        btn_history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHistory();
            }
        });
    }

    public static void main(String[] args) {

    }

    public void View(){
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("CalculatorForm");
        frame.setContentPane(new CalculatorForm(lbl_username.getText()).panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    private void CheckFontSize(){
        int length = lbl_result.getText().length();
        if (length > 10) {
            float scale = 0.94f;
            lbl_result.setFont(lbl_result.getFont().deriveFont(lbl_result.getFont().getSize() * scale));
        }
        else{
            lbl_result.setFont(lbl_result.getFont().deriveFont(30f));
        }
    }

    private void AddNumber(int n){
        String text = lbl_result.getText();
        String newtext = lbl_result.getText();

        if(text.equals("0")){
            newtext = "";
        }

        if(!rpn){
            if(text.endsWith(")")){
                newtext = text.substring(0, text.length() - 1);
            }
        }
        newtext += Integer.toString(n);
        lbl_result.setText(newtext);

        CheckFontSize();
    }

    private void AddSign(char sign) {
        String text = lbl_result.getText();
        String newtext = lbl_result.getText();

        if(!rpn){
            char lastchar = text.charAt(text.length() - 1);

            if(sign == '('){
                if(lastchar == ','){
                    return;
                }
                else if(lastchar == '0'){
                    newtext = "";
                }
                else if(!IsOperator(lastchar) && lastchar != '('){
                    newtext += "×";
                }
            }
            else if(sign == ')'){
                if(CountOccurrences(text, '(') > CountOccurrences(text, ')')){
                    if(IsOperator(lastchar) || lastchar == '(' || lastchar == ','){
                        return;
                    }
                }
                else{
                    return;
                }
            }
            else if(sign == ','){
                if(IsOperator(lastchar) || lastchar == ')' || lastchar == '(' || lastchar == ','){
                    return;
                }
            }
            else if(IsOperator(lastchar) || lastchar == '(' || lastchar == ','){
                if(lastchar == '('){
                    return;
                }
                newtext = newtext.substring(0, newtext.length() - 1);
            }
        }
        newtext += sign;
        lbl_result.setText(newtext);

        CheckFontSize();
    }

    private boolean IsOperator(char c) {
        return c == '+' || c == '-' || c == '×' || c == '/';
    }

    private int CountOccurrences(String text, char targetChar) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == targetChar) {
                count++;
            }
        }
        return count;
    }

    private void Reset(){
        lbl_result.setText("0");
        CheckFontSize();
    }

    private void Result(){
        LoadVars();
        lbl_result.setText(Double.toString(RPNtoResult()));

        addToHistory(lbl_username.getText(), String.valueOf(expression));
        expression.clear();
        CheckFontSize();
    }

    private void LoadVars(){
        String text = lbl_result.getText();
        StringBuilder currentNumber = new StringBuilder();

        char c;

        if(!rpn){
            Boolean brackets = false;
            Boolean precedence = false;

            for(int i = 0; i < text.length(); i++){
                c = text.charAt(i);

                if(Character.isDigit(c)){
                    currentNumber.append(c);
                }
                else if(c == ',' || c == '.'){
                    currentNumber.append('.');
                }
                else if(IsOperator(c)){
                    if(!currentNumber.isEmpty()){
                        expression.add(currentNumber.toString());
                        currentNumber.setLength(0);
                    }

                    if((precedence && !brackets) || (brackets && precedence && i < text.length() - 1)){
                        Insert();
                        precedence = false;
                    }
                    operators.push(c);

                    if(HasPrecedence(c)){
                        precedence = true;
                    }
                }
                else if(c == '('){
                    operators.push(c);

                    brackets = true;
                    precedence = false;
                }
                else if(c == ')'){
                    if(!currentNumber.isEmpty()){
                        expression.add(currentNumber.toString());
                        currentNumber.setLength(0);
                    }
                    precedence = false;

                    Insert();
                    Insert();
                }
            }

            if(!currentNumber.isEmpty()){
                expression.add(currentNumber.toString());
            }

            if(!operators.isEmpty()){
                Insert();
            }
        }
        else{
            for(int i = 0; i < text.length(); i++) {
                c = text.charAt(i);

                if(Character.isDigit(c)){
                    currentNumber.append(c);
                }
                else{
                    if(!currentNumber.isEmpty()){
                        expression.add(currentNumber.toString());
                        currentNumber.setLength(0);
                    }
                    expression.add(Character.toString(c));
                }
            }

            if(!currentNumber.isEmpty()){
                expression.add(currentNumber.toString());
                currentNumber.setLength(0);
            }
        }
    }

    private void Insert(){
        while(!operators.isEmpty() && operators.peek() != '('){
            expression.add(operators.pop().toString());
        }

        if(!operators.isEmpty()){
            operators.pop();
        }
    }

    private Boolean HasPrecedence(char c){
        return switch (c) {
            case '×', '/' -> true;
            default -> false;
        };
    }

    private double RPNtoResult() {
        Stack<Double> stack = new Stack<>();

        double operand1;
        double operand2;

        for (String token : expression) {
            if (IsNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (IsOperator(token.charAt(0))) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Operands missing for operator " + token);
                }
                operand2 = stack.pop();
                operand1 = stack.pop();

                stack.push(ApplyOperator(operand1, operand2, token));
            }
        }
        return stack.pop();
    }

    private boolean IsNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private double ApplyOperator(double operand1, double operand2, String operator) {
        return switch (operator) {
            case "+" -> operand1 + operand2;
            case "-" -> operand1 - operand2;
            case "×" -> operand1 * operand2;
            case "/" -> {
                if (operand2 == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                yield operand1 / operand2;
            }
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    private void showHistory() {
        String username = lbl_username.getText();
        String history = LoginForm.DB.getHistory(username);
        JOptionPane.showMessageDialog(panel, "History for " + username + ":\n" + history, "History", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addToHistory(String username, String expression) {
        LoginForm.DB.addToHistory(username, expression);
    }
}
