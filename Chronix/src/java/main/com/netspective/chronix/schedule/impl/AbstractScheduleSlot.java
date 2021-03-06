/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package com.netspective.chronix.schedule.impl;

import java.util.Date;

import com.netspective.chronix.CalendarUtils;
import com.netspective.chronix.schedule.model.ScheduleManager;
import com.netspective.chronix.schedule.model.ScheduleSlot;
import com.netspective.chronix.set.MinuteRangesSet;

public abstract class AbstractScheduleSlot implements ScheduleSlot, Comparable
{
    private ScheduleManager scheduleManager;
    private Object identifier = new Integer(hashCode());
    private Date beginDate;
    private int beginJulianDay;
    private Date endDate;
    private int endJulianDay;
    private MinuteRangesSet minutesSet;

    public AbstractScheduleSlot(ScheduleManager scheduleManager, Date beginDate, Date endDate)
    {
        this.scheduleManager = scheduleManager;

        CalendarUtils calendarUtils = scheduleManager.getCalendarUtils();

        this.beginDate = beginDate;
        this.endDate = endDate;
        this.beginJulianDay = calendarUtils.getJulianDay(beginDate);
        this.endJulianDay = calendarUtils.getJulianDay(endDate);
        this.minutesSet = new MinuteRangesSet(calendarUtils);
        this.minutesSet.applyDateRange(beginDate, endDate);
    }

    public int compareTo(Object o)
    {
        ScheduleSlot compareTo = (ScheduleSlot) o;

        long myStartDay = this.getBeginJulianDay();
        long myEndDay = this.getEndJulianDay();

        long compareStartDay = compareTo.getBeginJulianDay();
        long compareEndDay = compareTo.getEndJulianDay();

        if(myStartDay < compareStartDay)
            return -1;

        if(myStartDay > compareStartDay)
            return 1;

        if(myEndDay < compareEndDay)
            return -1;

        if(myEndDay > compareEndDay)
            return 1;

        long myStartTime = this.getBeginDate().getTime();
        long myEndTime = this.getEndDate().getTime();

        long compareStartTime = compareTo.getBeginDate().getTime();
        long compareEndTime = compareTo.getEndDate().getTime();

        if(myStartTime < compareStartTime)
            return -1;

        if(myStartTime > compareStartTime)
            return 1;

        return 0;
    }

    public abstract boolean isOpenSlot();

    public Object getScheduleSlotIdentifier()
    {
        return identifier;
    }

    public ScheduleManager getScheduleManager()
    {
        return scheduleManager;
    }

    public boolean isMultipleDays()
    {
        return minutesSet.isMultipleDays();
    }

    public Date getDate()
    {
        return beginDate;
    }

    public int getJulianDay()
    {
        return beginJulianDay;
    }

    public Date getBeginDate()
    {
        return beginDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public int getBeginJulianDay()
    {
        return beginJulianDay;
    }

    public int getEndJulianDay()
    {
        return endJulianDay;
    }

    public MinuteRangesSet getMinutesSet()
    {
        return minutesSet;
    }

    public String toString()
    {
        CalendarUtils calendarUtils = getScheduleManager().getCalendarUtils();
        return getClass().getName() + ": id = " + identifier + ", begin = " + calendarUtils.formatDateTime(beginDate) + ", end = " + calendarUtils.formatDateTime(endDate) + ", minutes = " + minutesSet;
    }
}
