.source "T_mul_float_2addr_5.java"
.class  public Ldot/junit/opcodes/mul_float_2addr/d/T_mul_float_2addr_5;
.super  Ljava/lang/Object;


.method public constructor <init>()V
.registers 1

       invoke-direct {v0}, Ljava/lang/Object;-><init>()V
       return-void
.end method

.method public run(FF)F
.registers 8

       mul-float/2addr v5, v7
       return v5
.end method
