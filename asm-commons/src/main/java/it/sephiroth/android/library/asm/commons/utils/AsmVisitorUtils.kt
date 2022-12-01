package it.sephiroth.android.library.asm.commons.utils

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

object AsmVisitorUtils {
    fun visitInt(mv: MethodVisitor, value: Int) {
        val level = Opcodes.ICONST_0 + value
        if (level > Opcodes.ICONST_5) {
            mv.visitIntInsn(Opcodes.BIPUSH, value)
        } else {
            mv.visitInsn(level)
        }
    }

    fun getLoadOpcodeFromDesc(desc: String): Int {
        var opcode = Opcodes.ILOAD
        if ("F" == desc) {
            opcode = Opcodes.FLOAD
        } else if ("J" == desc) {
            opcode = Opcodes.LLOAD
        } else if ("D" == desc) {
            opcode = Opcodes.DLOAD
        } else if (desc.startsWith("L")) {  //object
            opcode = Opcodes.ALOAD
        } else if (desc.startsWith("[")) {  //array
            opcode = Opcodes.ALOAD
        }
        return opcode
    }

    fun getStoreOpcodeFromType(type: Type): Int {
        var opcode = Opcodes.ISTORE
        when (type.sort) {
            Type.LONG -> opcode = Opcodes.LSTORE
            Type.FLOAT -> opcode = Opcodes.FSTORE
            Type.DOUBLE -> opcode = Opcodes.DSTORE

            Type.OBJECT,
            Type.ARRAY ->
                opcode = Opcodes.ASTORE
        }
        return opcode
    }

    fun getLoadOpcodeFromType(type: Type): Int {
        var opcode = Opcodes.ILOAD
        when (type.sort) {
            Type.LONG -> opcode = Opcodes.LLOAD
            Type.FLOAT -> opcode = Opcodes.FLOAD
            Type.DOUBLE -> opcode = Opcodes.DLOAD

            Type.OBJECT,
            Type.ARRAY ->
                opcode = Opcodes.ALOAD
        }
        return opcode
    }
}
