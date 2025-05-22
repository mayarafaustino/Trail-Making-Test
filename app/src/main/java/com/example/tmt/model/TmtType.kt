package com.example.tmt.model

enum class TmtType(val maxDurationSeconds: Int)
{
    TMT_A(150),
    TMT_B(300),
    TMT_A_SAMPLE(300),
    TMT_B_SAMPLE(300);

    fun isSample(): Boolean
    {
        return this == TMT_A_SAMPLE || this == TMT_B_SAMPLE
    }

    fun getName(): String
    {
        return when(this)
        {
            TMT_A -> "TMT A"
            TMT_B -> "TMT B"
            TMT_A_SAMPLE -> "TMT A Sample"
            TMT_B_SAMPLE -> "TMT B Sample"
        }
    }
}
