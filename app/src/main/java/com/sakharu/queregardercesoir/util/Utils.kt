package com.sakharu.queregardercesoir.util




class Utils
{
    companion object
    {
        fun differenceInDayFromActualTimestamp(oldTimestamp:Long) : Long
        {
            return  (oldTimestamp / (24 * 60 * 60 * 1000)
                    - (System.currentTimeMillis() / (24 * 60 * 60 * 1000)))
        }
    }
}