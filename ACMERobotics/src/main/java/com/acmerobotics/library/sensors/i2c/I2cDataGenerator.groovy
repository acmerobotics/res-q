package com.acmerobotics.library.sensors.i2c

import com.acmerobotics.library.util.FileUtils
import com.sun.codemodel.ClassType
import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JExpr

import static com.sun.codemodel.JMod.*;

public class I2cDataGenerator {

    public static void generateI2cDataClasses(final File assetChipDir, final File outputDir) {
        def codeModel = new JCodeModel();

        String[] files = assetChipDir.list();
        for (String file : files) {
            String contents = FileUtils.getInputStreamContents(new FileInputStream(file));
            I2cChipData chipData = I2cChipJSONParser.parseJson(contents);

            generateClass(chipData).build(outputDir);
        }
    }

    private static JCodeModel generateClass(I2cChipData data) {
        def codeModel = new JCodeModel()

        def fqcn = I2cDataGenerator.class.getPackage().getName() + data.name
        def i2cDataClass = codeModel._class(PUBLIC | FINAL, fqcn, ClassType.CLASS)

        def nameField = i2cDataClass.field(PUBLIC | STATIC | FINAL, codeModel.ref(String.class), 'NAME', JExpr.lit(data.name));
        def manufacturerField = i2cDataClass.field(PUBLIC | STATIC | FINAL, codeModel.ref(String.class), 'MANUFACTURER', JExpr.lit(data.manufacturer));

        def addressArray = JExpr.newArray(codeModel.parseType('int'))
        for (int address : data.addresses) {
            addressArray.add(JExpr.lit(address))
        }
        def addressField = i2cDataClass.field(PUBLIC | STATIC | FINAL, codeModel.parseType('int[]'), 'ADDRESSES', addressArray)

        def registerClass = i2cDataClass._class(PUBLIC | STATIC | FINAL, 'Registers')
        for (String register : data.registers.keySet()) {
            registerClass.field(PUBLIC | STATIC | FINAL, codeModel.parseType('int'), register, JExpr.lit(data.registers.get(register)))
        }

        def extraClass = i2cDataClass._class(PUBLIC | STATIC | FINAL, 'Extra')
        for (String extra : data.extra.keySet()) {
            extraClass.field(PUBLIC | STATIC | FINAL, codeModel.ref(String.class), extra, JExpr.lit(data.extra.get(extra)))
        }
    }

}
