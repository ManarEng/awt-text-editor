
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Scanner;

public class EditeurText extends Frame implements ActionListener{
    String[] PoliceDecaractère = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    String famillepolices ;
    String policeactuelle;
    int taillepolice;
    Field[] fields;
    MenuItem ElémentDeMenuPolice; //sous menu de style
    MenuItem ElémentDeMenuColor; //sous menu de ColorOfLetters
    TextArea textArea;

    public EditeurText() {
        taillepolice = 20;
        MenuBar menuBar = new MenuBar();
        textArea = new TextArea();
        setMenuBar(menuBar);
        add(textArea);
        setTitle("Bloc Notes");
        setSize(1000, 600);
        setVisible(true);

        //creation des elements du menu
        Menu file = new Menu("File", true);
        Menu edit = new Menu("Edit");
        Menu style = new Menu("style");
        Menu ColorOfLetters = new Menu("Color");

        //ajout des elements au menu bar
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(style);
        menuBar.add(ColorOfLetters);


        //creation des sous menu de file
        MenuItem New = new MenuItem("New");
        MenuItem Open = new MenuItem("Open");
        MenuItem Close = new MenuItem("Close");
        MenuItem SaveAs = new MenuItem("Save as");
        MenuItem Exit = new MenuItem("Exit");

        //ajout des sous menu a file
        file.add(New);
        file.addSeparator();
        file.add(Open);
        file.addSeparator();
        file.add(Close);
        file.addSeparator();
        file.add(SaveAs);
        file.addSeparator();
        file.add(Exit);

        //creation des sous menu de edit
        MenuItem selectAll = new MenuItem("selectAll");
        MenuItem zoomtaille=new  MenuItem("zoom +");
        MenuItem diminutaille = new MenuItem("zoom -");

        //ajout des sous menu a edit
        edit.add(selectAll);
        edit.addSeparator();
        edit.add(zoomtaille);
        edit.addSeparator();
        edit.add(diminutaille);

        //creation des sous menu de style
        MenuItem Plain = new MenuItem("Plain");
        MenuItem Bold = new MenuItem("Bold");
        MenuItem Italic = new MenuItem("Italic");

        //ajout des sous menu a style
        style.add(Plain);
        style.addSeparator();
        style.add(Bold);
        style.addSeparator();
        style.add(Italic);
        style.addSeparator();


        //parcourir les styles d'ecriture des polices
        for (String fontFamily : PoliceDecaractère)
        {
            ElémentDeMenuPolice = new MenuItem(fontFamily);
            ElémentDeMenuPolice.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textArea.setFont(new Font(fontFamily, Font.PLAIN, taillepolice));
                }
            });
            style.add(ElémentDeMenuPolice);
        }

        fields = Color.class.getFields();

        //parcourir les couleurs des polices
        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) && java.lang.reflect.Modifier.isFinal(field.getModifiers()) && field.getType() == Color.class)
            {
                try
                {
                    final Color color = (Color) field.get(null);
                    MenuItem colorMenuItem = new MenuItem(field.getName());
                    ColorOfLetters.add(colorMenuItem);
                    colorMenuItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            textArea.setForeground(color);
                        }
                    });
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }

//donner des actions aux sous menus de file
        New.addActionListener(this);
        Open.addActionListener(this);
        Close.addActionListener(this);
        SaveAs.addActionListener(this);
        Exit.addActionListener(this);

//donner des actions aux sous menus de edit
        selectAll.addActionListener(this);
        zoomtaille.addActionListener(this);
        diminutaille.addActionListener(this);

//donner des actions aux sous menus de style
        ElémentDeMenuPolice.addActionListener(this);
        Plain.addActionListener(this);
        Bold.addActionListener(this);
        Italic.addActionListener(this);
//donner des actions aux sous menus de color
        ElémentDeMenuColor.addActionListener(this);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }


    //events sur Plain Bold Italic
    @Override
    public void actionPerformed(ActionEvent e) {
        //evennement de style
        Frame frame = new Frame();
        if (e.getActionCommand().equals("Plain")){
            textArea.setFont(new Font(famillepolices , Font.PLAIN, taillepolice));
        }
        if (e.getActionCommand().equals("Bold")){
            textArea.setFont(new Font(famillepolices , Font.BOLD, taillepolice));
        }
        if (e.getActionCommand().equals("Italic")){
            textArea.setFont(new Font(famillepolices , Font.ITALIC, taillepolice));
        }


        //evennement de edit
        if (e.getActionCommand().equals("zoom +"))
        {
            int fontSizeTemp =  taillepolice;
            fontSizeTemp += 5;
            textArea.setFont(new Font(policeactuelle ,Font.PLAIN, fontSizeTemp));
            taillepolice = fontSizeTemp;

        }
        if (e.getActionCommand().equals("zoom -"))
        {
            int fontSizeTemp = taillepolice;
            fontSizeTemp -= 5;
            textArea.setFont(new Font(policeactuelle ,Font.PLAIN, fontSizeTemp));
            taillepolice = fontSizeTemp;

        }
        if (e.getActionCommand().equals("selectAll")) {
            textArea.selectAll();
        }



        //evennemnt de file
        if (e.getActionCommand().equals("New"))
        {
            setTitle("Bloc Notes");
            textArea.setText("");
        }

        /* ********* */
        if (e.getActionCommand().equals("Open"))
        {
            String data = null;
            FileDialog fileLoadDialog = new FileDialog(frame, "loadDialog", FileDialog.LOAD);
            fileLoadDialog.setVisible(true);
            try
            {
                File loadedFile = new File(fileLoadDialog.getDirectory() + "\\" + fileLoadDialog.getFile());
                Scanner fileScanner = new Scanner(loadedFile);
                while(fileScanner.hasNextLine()){
                    data = fileScanner.nextLine();
                }
                textArea.append(data);
                fileScanner.close();

            }
            catch (FileNotFoundException ex)
            {
                throw new RuntimeException(ex);
            }

        }

        if (e.getActionCommand().equals("Save as"))
        {
            FileDialog fileLoadDialog = new FileDialog(frame, "saveDialog", FileDialog.SAVE);
            fileLoadDialog.setVisible(true);
            File savedFile = new File(fileLoadDialog.getDirectory()+ "\\" + fileLoadDialog.getFile());
            try
            {
                FileWriter fileWriter = new FileWriter(savedFile);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(textArea.getText());
                bufferedWriter.close();
            }
            catch(Exception ex)
            {
                throw new RuntimeException(ex);
            }

        }

        if (e.getActionCommand().equals("Close") || e.getActionCommand().equals("Exit"))
        {
            dispose();
        }


    }
}

