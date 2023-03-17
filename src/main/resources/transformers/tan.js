/*******************************************************************************
 * Copyright 2021, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
 var ASM = Java.type("net.minecraftforge.coremod.api.ASMAPI");

 var Opcodes = Java.type('org.objectweb.asm.Opcodes');
 var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
 var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
 var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

var BLIT = ASM.mapMethod("m_93228_");

function Transformation(name, desc) {
     this.name = name;
     this.desc = desc;
     this.funcs = [];
 
     for (var i = 2; i < arguments.length; i++) {
         this.funcs.push(arguments[i]);
     }
 }
 
 var TRANSFORMATIONS = {
    "net/minecraft/world/entity/player/Player": [
        new Transformation(ASM.mapMethod("m_36399_"), "(F)V", patchCauseFoodExhaustion) //causeFoodExhaustion
    ],
    "net/minecraft/world/food/FoodData": [
        new Transformation(ASM.mapMethod("m_38710_"), "(Lnet/minecraft/world/entity/player/Player;)V", patchFoodDataTick) //tick
    ]
 };
 
 function applyTransformations(classNode, method) {
     if (!(classNode.name in TRANSFORMATIONS)) {
         return;
     }
 
     var transformations = TRANSFORMATIONS[classNode.name];
 
     for (var i = 0; i < transformations.length; i++) {
         var transformation = transformations[i];
 
         if (transformation.name == method.name && transformation.desc == method.desc) {
             log("Transforming " + method.name + " " + method.desc + " in " + classNode.name);
 
             for each (var func in transformation.funcs) {
                 func(method);
             }
             break;
         }
     }
 }
 
 function log(message) {
     print("[Tough As Nails Transformer]: " + message);
 }
 
 function initializeCoreMod() {
     return {
         "thirst_transformer": {
             "target": {
                 "type": "CLASS",
                 "names": function(listofclasses) { return Object.keys(TRANSFORMATIONS); }
             },
             "transformer": function(classNode) {
                 for each (var method in classNode.methods) {
                     applyTransformations(classNode, method);
                 }
 
                 return classNode;
             }
         }
     };
 }

 function patchCauseFoodExhaustion(node) {
    var insns = new InsnList();
    insns.add(new VarInsnNode(Opcodes.ALOAD, 0));
    insns.add(new VarInsnNode(Opcodes.FLOAD, 1));
    insns.add(ASM.buildMethodCall(
        "toughasnails/thirst/ThirstHooks",
        "onCauseFoodExhaustion",
        "(Lnet/minecraft/world/entity/player/Player;F)V",
        ASM.MethodType.STATIC
    ));
    node.instructions.insertBefore(node.instructions.getFirst(), insns);
    log("Successfully patched causeFoodExhaustion");
 }

 function patchFoodDataTick(node) {
    var insns = new InsnList();
    insns.add(new VarInsnNode(Opcodes.ALOAD, 0));
    insns.add(new VarInsnNode(Opcodes.ALOAD, 1));
    insns.add(ASM.buildMethodCall(
        "toughasnails/thirst/ThirstHooks",
        "doFoodDataTick",
        "(Lnet/minecraft/world/food/FoodData;Lnet/minecraft/world/entity/player/Player;)V",
        ASM.MethodType.STATIC
    ));
    insns.add(new InsnNode(Opcodes.RETURN));
    node.instructions.insertBefore(node.instructions.getFirst(), insns);
    log("Successfully patched tick");
 }