package com.acmerobotics.library.sensors.i2c

import com.sun.codemodel.ClassType
import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JExpr
import com.sun.codemodel.JExpression
import com.sun.codemodel.JStatement
import com.sun.codemodel.JTryBlock

import static com.sun.codemodel.JMod.*;

public class I2cDataGenerator {

    public static void generateI2cData(final File generatedDir, final File chipDir) {
        if (!generatedDir.exists()) {
            generatedDir.mkdirs();
        }

        String[] files = chipDir.list();
        for (String file : files) {
            String contents = FileUtils.getInputStreamContents(new FileInputStream(chipDir.getAbsolutePath() + "/" + file));
            I2cChipData chipData = I2cChipJSONParser.parseJson(contents);

            generateClass(chipData).build(generatedDir);
        }
    }

    private static JCodeModel generateClass(I2cChipData data) {
        def codeModel = new JCodeModel()

        def fqcn = I2cDataGenerator.class.getPackage().getName() + "." + data.name
        def i2cDataClass = codeModel._class(PUBLIC | FINAL, fqcn, ClassType.CLASS)

        def nameField = i2cDataClass.field(PUBLIC | STATIC | FINAL, codeModel.ref(String.class), 'NAME', JExpr.lit(data.name));
        def manufacturerField = i2cDataClass.field(PUBLIC | STATIC | FINAL, codeModel.ref(String.class), 'MANUFACTURER', JExpr.lit(data.manufacturer));

        def addressArray = JExpr.newArray(codeModel.INT)
        for (int address : data.addresses) {
            addressArray.add(JExpr.lit(address))
        }
        def addressField = i2cDataClass.field(PUBLIC | STATIC | FINAL, codeModel.parseType('int[]'), 'ADDRESSES', addressArray)

        def registerClass = i2cDataClass._class(PUBLIC | STATIC | FINAL, 'Registers')
        for (String register : data.registers.keySet()) {
            registerClass.field(PUBLIC | STATIC | FINAL, codeModel.INT, register, JExpr.lit(data.registers.get(register)))
        }

        def registerMethod = registerClass.method(PUBLIC | STATIC | FINAL, codeModel.INT, "getRegister")
        def regArg = registerMethod.param(codeModel._ref(String.class), "reg")
        def tryBlock = new JTryBlock()
        def catchBlock = tryBlock._catch(codeModel.ref(Exception.class))
        def getFieldInvocation = JExpr.invoke(registerClass.dotclass(), "getField").arg(regArg).invoke("get").arg(JExpr._null())
        tryBlock.body()._return(JExpr.cast(codeModel.INT, getFieldInvocation))
        def registerMethodBody = registerMethod.body().add(tryBlock)._return(JExpr.lit(0))


        def extraClass = i2cDataClass._class(PUBLIC | STATIC | FINAL, 'Extra')
        for (String extra : data.extra.keySet()) {
            extraClass.field(PUBLIC | STATIC | FINAL, codeModel.ref(String.class), extra, JExpr.lit(data.extra.get(extra)))
        }

        return codeModel
    }

}
