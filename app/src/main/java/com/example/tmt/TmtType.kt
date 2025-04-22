package com.example.tmt

enum class TmtType()
{
    TMT_A,
    TMT_B,
    TMT_A_SAMPLE,
    TMT_B_SAMPLE;

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
