/**
 * @copyright 2006 IEX, A Tekelec Company
 * @author tlark
 * @version "%I%, %G%"
 */
package com.iex.tv.services.impl.core.model.dao.hibernate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.iex.tv.core.datetime.DateRange;
import com.iex.tv.core.datetime.Month;
import com.iex.tv.core.datetime.TimeRange;
import com.iex.tv.core.datetime.Year;
import com.iex.tv.core.datetime.YearMonth;
import com.iex.tv.core.framework.TvLogger;
import com.iex.tv.core.utils.Utils;
import com.iex.tv.domain.support.FieldCriteria;
import com.iex.tv.domain.support.FilterCriteria;
import com.iex.tv.domain.support.IdName;
import com.iex.tv.domain.support.NamedQueryMetaData;
import com.iex.tv.domain.support.SortCriteria;
import com.iex.tv.domain.support.TvPersistable;
import com.iex.tv.schema.utils.SchemaUtils;
import com.iex.tv.services.impl.core.model.dao.TvDaoException;
import com.iex.tv.services.impl.core.model.utils.NameValue;

public final class HibernateUtils
{
    private static final TvLogger logger = new TvLogger(HibernateUtils.class);

    /**
     * Max #values to allow in an SQL 'IN' expression.
     */
    private static final int inexpression_max_value = 500;

    /**
     * Adds date range restrictions to the given criteria based on a single date. <br>
     * Handles records with an open (null) end date.
     * 
     * @param startDatePropertyName
     * @param endDatePropertyName
     * @param date
     * @param criteria
     */
    public static final void addDateRangeRestrictions(String startDatePropertyName, String endDatePropertyName,
            LocalDate date, DetachedCriteria criteria)
    {
        if (!Utils.isBlank(startDatePropertyName) && !Utils.isBlank(endDatePropertyName) && (criteria != null))
        {
            if (date != null)
            {
                // sdate <= date
                // AND ((edate is null) || (edate >= date))
                criteria.add(Restrictions.and(
                    Restrictions.le(startDatePropertyName, date),
                    Restrictions.or(Restrictions.isNull(endDatePropertyName),
                        Restrictions.ge(endDatePropertyName, date))));
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), startDatePropertyName=" + startDatePropertyName + ", endDatePropertyName="
                    + endDatePropertyName + ", date=" + date + ", criteria=" + criteria);
        }
    }

    /**
     * Adds date range restrictions to the given criteria based on a single date. <br>
     * Handles records with an open (null) end date.
     * 
     * @param startDatePropertyName
     * @param endDatePropertyName
     * @param date
     * @param criteria
     */
    public static final Criterion getDateRangeRestrictions(String startDatePropertyName, String endDatePropertyName,
            LocalDate date)
    {
        if (!Utils.isBlank(startDatePropertyName) && !Utils.isBlank(endDatePropertyName))
        {
            if (date != null)
            {
                // sdate <= date
                // AND ((edate is null) || (edate >= date))
                return Restrictions.and(
                    Restrictions.le(startDatePropertyName, date),
                    Restrictions.or(Restrictions.isNull(endDatePropertyName),
                        Restrictions.ge(endDatePropertyName, date)));
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), startDatePropertyName=" + startDatePropertyName + ", endDatePropertyName="
                    + endDatePropertyName + ", date=" + date);
        }
        return null;
    }

    /**
     * Adds year month restrictions to the given criteria based on a single year month. <br>
     * 
     * @param yearPropertyName
     * @param monthPropertyName
     * @param yearMonthParm
     * @param criteria
     */
    public static final Criterion getYearMonthRestrictions(String yearMonthPropertyName, YearMonth yearMonthParm)
    {
        if (!Utils.isBlank(yearMonthPropertyName))
        {
            if (yearMonthParm != null)
            {
                // c_year eq year and c_month = month
                return Restrictions.and(
                    Restrictions.eq(yearMonthPropertyName + ".year", yearMonthParm.getYear().shortValue()),
                    Restrictions.eq(yearMonthPropertyName + ".month", yearMonthParm.getMonth().toString()));
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), yearMonthPropertyName=" + yearMonthPropertyName + ", yearMonthParm="
                    + yearMonthParm);
        }
        return null;
    }

    /**
     * Adds year month restrictions to the given criteria based on a year month range. <br>
     * 
     * @param yearPropertyName
     * @param monthPropertyName
     * @param startYearMonthParm
     * @param endYearMonthParm
     * @param criteria
     */
    public static final Criterion getYearMonthRangeRestrictions(String yearMonthPropertyName,
            YearMonth startYearMonthParm, YearMonth endYearMonthParm)
    {
        if (!Utils.isBlank(yearMonthPropertyName))
        {
            if (startYearMonthParm != null && endYearMonthParm != null)
            {
                Map<Year, Set<Month>> yearMonthMap = YearMonth.getYearMonthMap(startYearMonthParm, endYearMonthParm);
                if (!Utils.isEmpty(yearMonthMap))
                {
                    Criterion criterion = null;
                    Set<Year> yearSet = yearMonthMap.keySet();
                    for (Year year : yearSet)
                    {
                        Set<Month> monthSet = yearMonthMap.get(year);
                        Criterion newCriterion = Restrictions.and(
                            Restrictions.eq(yearMonthPropertyName + ".year", year.shortValue()),
                            getCollectionRestriction(yearMonthPropertyName + ".month", Month.getStringSet(monthSet)));
                        if (criterion == null)
                        {
                            criterion = newCriterion;
                        }
                        else
                        {
                            criterion = Restrictions.or(criterion, newCriterion);
                        }
                    }
                    return criterion;
                }
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), yearMonthPropertyName=" + yearMonthPropertyName + ", startYearMonthParm="
                    + startYearMonthParm + ", endYearMonthParm=" + endYearMonthParm);
        }
        return null;
    }

    /**
     * Adds date range restrictions to the given criteria based on a date range. <br>
     * Handles records with an open (null) end date.
     * 
     * @param startDatePropertyName
     * @param endDatePropertyName
     * @param dateRange
     * @param criteria
     */
    public static final void addDateRangeRestrictions(String startDatePropertyName, String endDatePropertyName,
            DateRange dateRange, DetachedCriteria criteria)
    {
        if (!Utils.isBlank(startDatePropertyName) && !Utils.isBlank(endDatePropertyName) && (criteria != null))
        {
            // Ignore for null date ranges
            if (dateRange != null)
            {
                if (dateRange.isOpenEnded())
                {
                    // (edate is NULL) OR (edate >= dateRange.getStart())
                    criteria.add(Restrictions.or(Restrictions.isNull(endDatePropertyName),
                        Restrictions.ge(endDatePropertyName, dateRange.getStart())));
                }
                else
                {
                    // sdate <= dateRange.getEnd() AND ((edate is NULL) OR (edate >= dateRange.getStart()))
                    criteria.add(Restrictions.le(startDatePropertyName, dateRange.getEnd()));

                    criteria.add(Restrictions.or(Restrictions.isNull(endDatePropertyName),
                        Restrictions.ge(endDatePropertyName, dateRange.getStart())));
                }
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), startDatePropertyName=" + startDatePropertyName + ", endDatePropertyName="
                    + endDatePropertyName + ", dateRange=" + dateRange + ", criteria=" + criteria);
        }
    }

    /**
     * Adds date range restrictions to the given criteria based on a closed date range or default designation.
     * 
     * @param startDatePropertyName
     * @param endDatePropertyName
     * @param dateRange
     * @param criteria
     */
    public static final void addDateRangeAndDefaultRestrictions(String startDatePropertyName,
            String endDatePropertyName, DateRange dateRange, String defaultPropertyName, DetachedCriteria criteria)
    {
        if (!Utils.isBlank(startDatePropertyName) && !Utils.isBlank(endDatePropertyName) && (criteria != null))
        {
            boolean targetVal = true;
            // Ignore for null date ranges
            if (dateRange != null)
            {
                if (!dateRange.isOpenStarted() && !dateRange.isOpenEnded())
                {
                    // (sdate <= dateRange.getEnd() OR (edate >= dateRange.getStart()))) OR default is true
                    LogicalExpression inDateRange = Restrictions.or(
                        Restrictions.le(startDatePropertyName, dateRange.getEnd()),
                        Restrictions.ge(endDatePropertyName, dateRange.getStart()));
                    SimpleExpression isDefault = Restrictions.eq(defaultPropertyName, targetVal);
                    criteria.add(Restrictions.or(inDateRange, isDefault));
                }
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), startDatePropertyName=" + startDatePropertyName + ", endDatePropertyName="
                    + endDatePropertyName + ", dateRange=" + dateRange + ", defaultPropertyName=" + defaultPropertyName
                    + ", criteria=" + criteria);
        }
    }

    /**
     * Handles records with open start and/or open end date. Intended for use with ResourceLockEntry (most date ranges
     * don't allow an open start).
     * 
     * @param startDatePropertyName
     * @param endDatePropertyName
     * @param dateRange
     * @param criteria
     */
    public static final void addOpenDateRangeRestrictions(String startDatePropertyName, String endDatePropertyName,
            DateRange dateRange, DetachedCriteria criteria)
    {
        if (!Utils.isBlank(startDatePropertyName) && !Utils.isBlank(endDatePropertyName) && (criteria != null))
        {
            // Ignore for null date ranges,
            // ignore ranges open on both ends - no restriction will get all records
            if (dateRange != null && !(dateRange.isOpenStarted() && dateRange.isOpenEnded()))
            {
                if (dateRange.getEnd() != null)
                {
                    // (sdate is NULL) OR (sdate <= dateRange.getEnd())
                    criteria.add(Restrictions.or(Restrictions.isNull(startDatePropertyName), //
                        Restrictions.le(startDatePropertyName, dateRange.getEnd())));
                }
                // ... AND
                if (dateRange.getStart() != null)
                {
                    // (edate is NULL) OR (edate >= dateRange.getStart())
                    criteria.add(Restrictions.or(Restrictions.isNull(endDatePropertyName), //
                        Restrictions.ge(endDatePropertyName, dateRange.getStart())));
                }
            }
            logger.info(criteria);
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), startDatePropertyName=" + startDatePropertyName + ", endDatePropertyName="
                    + endDatePropertyName + ", dateRange=" + dateRange + ", criteria=" + criteria);
        }
    }

    /**
     * Adds date range restrictions to the given criteria using the dateColumn passed in for a subquery join.
     * 
     * @param startDatePropertyName the start date property name
     * @param endDatePropertyName the end date property name
     * @param criteria the criteria
     * @param dateColumn the date column
     */
    public static final void addDateRangeRestrictions(String startDatePropertyName, String endDatePropertyName,
            String dateColumn, DetachedCriteria criteria)
    {
        if (!Utils.isBlank(startDatePropertyName) && !Utils.isBlank(endDatePropertyName) && (criteria != null))
        {
            if (dateColumn != null)
            {
                // sdate <= date
                // AND ((edate is null) || (edate >= date))
                criteria.add(Restrictions.and(
                    Restrictions.leProperty(startDatePropertyName, dateColumn),
                    Restrictions.or(Restrictions.isNull(endDatePropertyName),
                        Restrictions.geProperty(endDatePropertyName, dateColumn))));
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), startDatePropertyName=" + startDatePropertyName + ", endDatePropertyName="
                    + endDatePropertyName + ", date=" + dateColumn + ", criteria=" + criteria);
        }
    }

    /**
     * Adds date range restrictions to the given criteria using the dateColumn passed in for a subquery join.
     * 
     * @param startDatePropertyName the start date property name
     * @param endDatePropertyName the end date property name
     * @param criteria the criteria
     * @param dateColumn the date column
     */
    public static final Criterion getDateRangeRestrictions(String startDatePropertyName, String endDatePropertyName,
            String dateColumn)
    {
        Criterion criterion = null;
        if (!Utils.isBlank(startDatePropertyName) && !Utils.isBlank(endDatePropertyName))
        {
            if (dateColumn != null)
            {
                // sdate <= date
                // AND ((edate is null) || (edate >= date))

                criterion = Restrictions.and(
                    Restrictions.leProperty(startDatePropertyName, dateColumn),
                    Restrictions.or(Restrictions.isNull(endDatePropertyName),
                        Restrictions.geProperty(endDatePropertyName, dateColumn)));
                return criterion;
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), startDatePropertyName=" + startDatePropertyName + ", endDatePropertyName="
                    + endDatePropertyName + ", date=" + dateColumn);
        }
        return null;
    }

    /**
     * Adds date restrictions to the given criteria based on a date range. The resulting criteria will select objects
     * with a date property such that the date is between (inclusive) of the date range.
     * <ul>
     * <li>If the date range is "open started", only object's whose date <= the range's end date will be selected.
     * <li>If the date range is "open ended", only object's whose date >= the range's start date will be selected.
     * </ul>
     * 
     * @param datePropertyName
     * @param dateRange
     * @param criteria
     */
    public static final void addDateRangeRestrictions(String datePropertyName, DateRange dateRange,
            DetachedCriteria criteria)
    {
        if (!Utils.isBlank(datePropertyName) && (criteria != null))
        {
            // Ignore for null date ranges
            if (dateRange != null)
            {
                // Use most efficient criteria
                if (dateRange.getDayCount() == 1)
                {
                    criteria.add(Restrictions.eq(datePropertyName, dateRange.getStart()));
                }
                else
                // Date range is more than one day
                {
                    if (dateRange.getStart() != null)
                    {
                        criteria.add(Restrictions.ge(datePropertyName, dateRange.getStart()));
                    }

                    if (dateRange.getEnd() != null)
                    {
                        criteria.add(Restrictions.le(datePropertyName, dateRange.getEnd()));
                    }
                }
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), datePropertyName=" + datePropertyName + ", dateRange=" + dateRange
                    + ", criteria=" + criteria);
        }
    }

    /**
     * Adds date restrictions to the given criteria based on a set of dates.
     * 
     * @param startDatePropertyName
     * @param endDatePropertyName
     * @param date
     * @param criteria
     */
    public static final void addDateRangeRestrictions(String startDatePropertyName, String endDatePropertyName,
            Set<LocalDate> dates, DetachedCriteria criteria)
    {
        if (!Utils.isBlank(startDatePropertyName) && !Utils.isBlank(endDatePropertyName) && (criteria != null))
        {
            if (dates != null)
            {
                LogicalExpression expression = null;
                for (LocalDate date : dates)
                {
                    // sdate <= date
                    // AND ((edate is null) || (edate >= date))

                    if (expression == null)
                    {
                        expression = Restrictions.and(
                            Restrictions.le(startDatePropertyName, date),
                            Restrictions.or(Restrictions.isNull(endDatePropertyName),
                                Restrictions.ge(endDatePropertyName, date)));

                    }
                    else
                    {
                        expression = Restrictions.or(
                            expression,
                            Restrictions.and(
                                Restrictions.le(startDatePropertyName, date),
                                Restrictions.or(Restrictions.isNull(endDatePropertyName),
                                    Restrictions.ge(endDatePropertyName, date))));
                    }

                }

                criteria.add(expression);
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), startDatePropertyName=" + startDatePropertyName + ", endDatePropertyName="
                    + endDatePropertyName + ", date=" + dates + ", criteria=" + criteria);
        }
    }

    /**
     * Get date range restrictions to the given criteria based on a set of dates. <br>
     * Handles records with an open (null) end date.
     * 
     * @param startDatePropertyName
     * @param endDatePropertyName
     * @param dates
     * @param criteria
     */
    public static final Criterion getDateRangeRestrictions(String startDatePropertyName, String endDatePropertyName,
            Set<LocalDate> dates)
    {

        Criterion criterion = null;
        if (!Utils.isBlank(startDatePropertyName) && !Utils.isBlank(endDatePropertyName))
        {
            if (dates != null)
            {
                for (LocalDate date : dates)
                {

                    if (criterion == null)
                    {
                        criterion = Restrictions.and(
                            Restrictions.le(startDatePropertyName, date),
                            Restrictions.or(Restrictions.isNull(endDatePropertyName),
                                Restrictions.ge(endDatePropertyName, date)));

                    }
                    else
                    {
                        criterion = Restrictions.or(
                            criterion,
                            Restrictions.and(
                                Restrictions.le(startDatePropertyName, date),
                                Restrictions.or(Restrictions.isNull(endDatePropertyName),
                                    Restrictions.ge(endDatePropertyName, date))));
                    }
                }
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), startDatePropertyName=" + startDatePropertyName + ", endDatePropertyName="
                    + endDatePropertyName + ", date=" + dates);
        }
        return criterion;
    }

    /**
     * Adds time range restrictions to the given criteria based on a time range. The resulting criteria will select
     * objects with a date property such that the date is between (inclusive) of the time range.
     * <ul>
     * <li>If the time range is "open started", only object's whose datetime <= the range's end datetime will be
     * selected.
     * <li>If the time range is "open ended", only object's whose datetime >= the range's start datetime will be
     * selected.
     * </ul>
     * 
     * @param timePropertyName
     * @param TimeRange
     * @param criteria
     */
    public static final void addTimeRangeRestrictions(String timePropertyName, TimeRange timeRange,
            DetachedCriteria criteria)
    {
        if (!Utils.isBlank(timePropertyName) && (criteria != null))
        {
            // Ignore for null time ranges
            if (timeRange != null)
            {

                if (timeRange.getStartTime() != null)
                {

                    criteria.add(Restrictions.ge(timePropertyName, timeRange.getStartTime()));
                }
                if (timeRange.getEndTime() != null)
                {
                    criteria.add(Restrictions.le(timePropertyName, timeRange.getEndTime()));
                }

            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), timePropertyName=" + timePropertyName + ", timeRange=" + timeRange
                    + ", criteria=" + criteria);
        }
    }

    /**
     * Adds interval restrictions to the given criteria based on an interval. The resulting criteria should select
     * records with an interval that overlaps the requested interval. It will not select intervals that are adjacent to,
     * but do not overlap, the requested interval.
     * 
     * @param intervalPropertyName name of the interval property in the class being selected.
     * @param interval The interval to select overlaps from.
     * @param criteria the criteria set to add overlap criteria to.
     */
    public static final void addIntervalOverlapRestrictions(String intervalPropertyName, Interval interval,
            DetachedCriteria criteria)
    {
        if (!Utils.isBlank(intervalPropertyName) && (criteria != null))
        {
            // Ignore for null intervals
            if (interval != null)
            {
                Timestamp intervalStart = SchemaUtils.instantToSqlTimestamp(interval.getStart().toInstant());
                Timestamp intervalEnd = SchemaUtils.instantToSqlTimestamp(interval.getEnd().toInstant());

                logger.debug("find overlap with [", intervalStart, ", ", intervalEnd, ")");
                criteria.add(Restrictions.and(Restrictions.lt(intervalPropertyName + ".startTime", intervalEnd),
                    Restrictions.gt(intervalPropertyName + ".endTime", intervalStart)));
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), intervalPropertyName=", intervalPropertyName, "interval=", interval,
                ", criteria=", criteria);
        }
    }

    /**
     * Adds interval restrictions to the given criteria based on an interval. The resulting criteria should select
     * records with an interval that overlaps or abuts the requested interval, such that if they were looked at as a
     * single interval, there would be no gaps in the entire range.
     * 
     * @param intervalPropertyName name of the interval property in the class being selected.
     * @param interval The interval to select overlaps from.
     * @param criteria the criteria set to add overlap criteria to.
     */
    public static final void addIntervalNoGapRestrictions(String intervalPropertyName, Interval interval,
            DetachedCriteria criteria)
    {
        if (!Utils.isBlank(intervalPropertyName) && (criteria != null))
        {
            // Ignore for null intervals
            if (interval != null)
            {
                Timestamp intervalStart = SchemaUtils.instantToSqlTimestamp(interval.getStart().toInstant());
                Timestamp intervalEnd = SchemaUtils.instantToSqlTimestamp(interval.getEnd().toInstant());

                logger.debug("find overlap with [", intervalStart, ", ", intervalEnd, ")");
                criteria.add(Restrictions.and(Restrictions.le(intervalPropertyName + ".startTime", intervalEnd),
                    Restrictions.ge(intervalPropertyName + ".endTime", intervalStart)));
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), intervalPropertyName=", intervalPropertyName, "interval=", interval,
                ", criteria=", criteria);
        }
    }

    /**
     * Adds start/end time restrictions to the given criteria given an interval. The resulting criteria should select
     * records with start/end times that overlap or abut the requested interval, such that if they were looked at as a
     * single interval, there would be no gaps in the entire range.
     * 
     * @param objectPropertyName name of the object property in the class being selected that has start/end times
     * @param interval The interval to select overlaps from.
     * @param criteria the criteria set to add overlap criteria to.
     */
    public static final void addStartEndTimesNoGapRestrictions(String objectPropertyName, Interval interval,
            DetachedCriteria criteria)
    {
        if (!Utils.isBlank(objectPropertyName) && (criteria != null))
        {
            // Ignore for null intervals
            if (interval != null)
            {
                Instant intervalStart = interval.getStart().toInstant();
                Instant intervalEnd = interval.getEnd().toInstant();

                logger.debug("find overlap with [", intervalStart, ", ", intervalEnd, ")");
                criteria.add(Restrictions.and(Restrictions.le(objectPropertyName + ".startTime", intervalEnd),
                    Restrictions.ge(objectPropertyName + ".endTime", intervalStart)));
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), objectPropertyName=", objectPropertyName, "interval=", interval,
                ", criteria=", criteria);
        }
    }

    /**
     * Adds interval restrictions to the given criteria based on an interval. The resulting criteria should select
     * records with an interval that is equal to the requested interval.
     * 
     * @param intervalPropertyName name of the interval property in the class being selected.
     * @param interval The interval to select overlaps from.
     * @param criteria the criteria set to add overlap criteria to.
     */
    public static final void addIntervalEqualsRestrictions(String intervalPropertyName, Interval interval,
            DetachedCriteria criteria)
    {
        if (!Utils.isBlank(intervalPropertyName) && (criteria != null))
        {
            // Ignore for null intervals
            if (interval != null)
            {
                Timestamp intervalStart = SchemaUtils.instantToSqlTimestamp(interval.getStart().toInstant());
                Timestamp intervalEnd = SchemaUtils.instantToSqlTimestamp(interval.getEnd().toInstant());

                logger.debug("find equals with [", intervalStart, ", ", intervalEnd, ")");
                criteria.add(Restrictions.and(Restrictions.eq(intervalPropertyName + ".startTime", intervalStart),
                    Restrictions.eq(intervalPropertyName + ".endTime", intervalEnd)));
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), intervalPropertyName=", intervalPropertyName, "interval=", interval,
                ", criteria=", criteria);
        }
    }

    /**
     * Adds interval restrictions to the given criteria based on an instant. The resulting criteria should select
     * records with an instant that is within the supplied interval. Instants that would be contiguous to the end of the
     * interval, but not included in the interval, will not be selected.
     * 
     * @param instantPropertyName name of the interval property in the class being selected.
     * @param interval The interval to select overlaps from.
     * @param criteria the criteria set to add overlap criteria to.
     */
    public static final void addIntervalInclusionRestrictions(String instantPropertyName, Interval interval,
            DetachedCriteria criteria)
    {
        if (!Utils.isBlank(instantPropertyName) && (criteria != null))
        {
            // Ignore for null intervals
            if (interval != null)
            {
                Instant intervalStart = interval.getStart().toInstant();
                Instant intervalEnd = interval.getEnd().toInstant();

                logger.debug("find inclusion in [", intervalStart, ", ", intervalEnd, ")");
                criteria.add(Restrictions.and(Restrictions.ge(instantPropertyName, intervalStart),
                    Restrictions.lt(instantPropertyName, intervalEnd)));
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), instantPropertyName=", instantPropertyName, "interval=", interval,
                ", criteria=", criteria);
        }
    }

    /**
     * Adds interval restrictions to the given criteria based on an instant. The resulting criteria should select
     * records with an interval that includes the supplied instant. Interval ends that would be contiguous to the
     * instant, but do not include the instant, will not be selected.
     * 
     * @param instantPropertyName name of the interval property in the class being selected.
     * @param interval The interval to select overlaps from.
     * @param criteria the criteria set to add overlap criteria to.
     */
    public static final void addIntervalInclusionRestrictions(String intervalPropertyName, Instant instant,
            DetachedCriteria criteria)
    {
        if (!Utils.isBlank(intervalPropertyName) && (criteria != null))
        {
            // Ignore for null instants
            if (instant != null)
            {
                Timestamp ts = SchemaUtils.instantToSqlTimestamp(instant);

                criteria.add(Restrictions.and(Restrictions.ge(intervalPropertyName + ".endTime", ts),
                    Restrictions.le(intervalPropertyName + ".startTime", ts)));
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), intervalPropertyName=", intervalPropertyName, "instant=", instant,
                ", criteria=", criteria);
        }
    }

    /**
     * Adds equality restrictions defined in array of name-value pairs. Name in the name-value pair is the name of the
     * property (field). Value is the value for this property to use in the restriction. If the value is null, no
     * restriction for the property is added.
     * 
     * @param criteria
     * @param nameValues
     */
    public static final void addPropertyValueRestrictions(DetachedCriteria criteria, NameValue... nameValues)
    {
        if (criteria != null && nameValues != null)
        {
            for (NameValue nameValue : nameValues)
            {
                String name = nameValue.getName();
                Object value = nameValue.getValue();
                if (value != null)
                {
                    criteria.add(Restrictions.eq(name, value));
                }
            }
        }
        else
        {
            logger.warn("Invalid input(s), criteria=", criteria, "nameValues=", nameValues);
        }
    }

    /**
     * Add the specified field options to the current criteria.
     * 
     * @param fieldOptions
     * @param mainCriteria
     * @deprecated As of 2006-08-28, use TvDaoHibernate.addFieldOptions.
     */
    public static final void addFieldOptions(Map<FieldCriteria.Key, FieldCriteria> fieldOptions,
            DetachedCriteria mainCriteria)
    {
        if (mainCriteria != null)
        {
            if (!Utils.isEmpty(fieldOptions))
            {
                // Each field criteria that refers to an assocation needs its own alias/criteria and disjunction filter
                Map<String, DetachedCriteria> associatedCriterias = new LinkedHashMap<String, DetachedCriteria>();
                Map<String, Disjunction> associatedFilters = new LinkedHashMap<String, Disjunction>();

                for (FieldCriteria fc : fieldOptions.values())
                {
                    DetachedCriteria targetCriteria = null;

                    // Is this field criteria for the main class or an associated class?
                    FieldCriteria.Key key = fc.getKey();
                    if (key.isAssociatedField())
                    {
                        DetachedCriteria associatedCriteria = associatedCriterias.get(key.getAssociation());
                        if (associatedCriteria == null)
                        {
                            associatedCriteria = mainCriteria.createCriteria(key.getAssociation());
                            associatedCriterias.put(key.getAssociation(), associatedCriteria);
                        }

                        targetCriteria = associatedCriteria;
                    }
                    else
                    // Not an associated field criteria
                    {
                        targetCriteria = mainCriteria;
                    }

                    // Ordered?
                    SortCriteria.Order order = fc.getOrder();
                    if (order.isOrdered())
                    {
                        if (order.isAscending())
                        {
                            targetCriteria.addOrder(Order.asc(key.getField()));
                        }
                        else
                        {
                            targetCriteria.addOrder(Order.desc(key.getField()));
                        }
                    }

                    // Filtered?
                    if (fc.isFiltered())
                    {
                        Disjunction filter = associatedFilters.get(key.getAssociation());
                        if (filter == null)
                        {
                            filter = Restrictions.disjunction();
                            associatedFilters.put(key.getAssociation(), filter);
                            targetCriteria.add(filter);
                        }

                        filter.add(Restrictions.ilike(key.getField(), fc.getFilter(), MatchMode.ANYWHERE));
                    }
                }
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), fieldOptions=" + fieldOptions + ", mainCriteria=" + mainCriteria);
        }
    }

    /**
     * Generic wrapper for executing a detached criteria query.
     * 
     * @param <T>
     * @param criteria
     * @param template
     * @return
     * @throws TvDaoException
     */
    @SuppressWarnings("unchecked")
//    public static final <T extends Object> List<T> findByCriteria(DetachedCriteria criteria, HibernateTemplate template)
//            throws TvDaoException
//    {
//        if ((criteria == null) && (template == null))
//        {
//            logger.warn("Invalid parm(s); findByCriteria(" + criteria + ", template=" + template + ")");
//            return null;
//        }
//
//        List<T> results = null;
//
//        try
//        {
//            logger.trace("findByCriteria=", criteria);
//
//            results = template.findByCriteria(criteria);
//
//            logger.debug("Found ", results.size(), " result(s), criteria=", criteria);
//        }
//        catch (Exception e)
//        {
//            logger.error(e, "findByCriteria(", criteria, ", template=", template, ")");
//            throw new TvDaoException("findByCriteria(" + criteria + ", template=" + template + ")", e);
//        }
//
//        return results;
//    }

    /**
     * Generic wrapper for executing a named query.
     * 
     * @param <T>
     * @param namedQuery
     * @param template
     * @return
     * @throws TvDaoException
     */
    @SuppressWarnings("unchecked")
    public static final <T extends Object> List<T> findByNamedQuery(NamedQueryMetaData namedQuery,
            HibernateTemplate template) throws TvDaoException
    {
        if ((namedQuery == null) || (template == null))
        {
            logger.warn("Invalid parm(s); findByNamedQuery(" + namedQuery + ", template=" + template + ")");
            return null;
        }

        List<T> results = null;
        logger.trace("findByNamedQuery=", namedQuery);

        // If any parameter has a large list, just log a warning. At this writing, the named queries
        // with 'IN' clauses that are passed here are unlikely to have long lists [WFM-5267]
        // 'find' queries with 'IN' clause:
        // AgentScheduleMinimal:
        // FindByTimeOffGroupAndDateRangeAndCodesQuery
        // ...."baseActivityCode.oid in (:oids) or d.activityCode.oid in (:oids)"
        // ManagementUnitDefinition:
        // FindMUOidsByPrefAgentDataValueQuery ...."prefAdv.agtDataValue in (:dataValueOids)"
        // FindTimeZonesForMusQuery ...."entity.oid in (:muOids) "
        Map<String, Collection<Object>> parmsWithLargeLists = //
        findLargeListValues("findByNamedQuery", namedQuery, inexpression_max_value);
        if (!Utils.isEmpty(parmsWithLargeLists))
        {
            logger.warn("  WARNING: query has large lists. ", namedQuery.getQueryName(), " names=",
                parmsWithLargeLists.keySet());
        }

        try
        {
            results = template.findByNamedQueryAndNamedParam(namedQuery.getQueryName(), namedQuery.getParamNames(),
                namedQuery.getParamValues());
            logger.info("Found ", results.size(), " result(s), namedQuery=", namedQuery);
        }
        catch (Exception e)
        {
            logger.error(e, "findByNamedQuery(", namedQuery, ", template=", template, ")");
            throw new TvDaoException("findByNamedQuery(" + namedQuery + ", template=" + template + ")", e);
        }

        return results;
    }

    /**
     * Executes update on a suitable named query (either HQL or native SQL): <br>
     * 1. Named query is retrieved by name <br>
     * 2. Named parameters are applied to the query <br>
     * If a parameter has a long list of values, create multiple queries that each work on a subset of the large list. <br>
     * (WFM-5467, follows WFM-4522) <br>
     * 3. Update is executed <br>
     * 
     * @param namedQueryMetaData
     * @param session
     * @throws TvDaoException
     */
    public static final int executeUpdateOnNamedQuery(NamedQueryMetaData namedQueryMetaData, Session session)
            throws TvDaoException
    {
        if (namedQueryMetaData == null || session == null)
        {
            String msg = "Invalid parm(s): " + namedQueryMetaData + ", session=" + session;
            logger.error(msg);
            throw new TvDaoException(msg);
        }

        // try {
        // Query query = session.getNamedQuery(namedQueryMetaData.getQueryName());
        // applyNamedParametersToQuery(query, namedQueryMetaData);
        // return query.executeUpdate();
        // }

        // get potentially many queries that each work on a subset of the criteria
        List<Query> queries = createQueriesFromNamedParameters("executeUpdateOnNamedQuery", session, namedQueryMetaData);
        int totalCnt = 0;
        for (Query query : queries)
        {
            try
            {
                int recCount = query.executeUpdate();
                logger.info(" updated ", recCount, " records based on ", namedQueryMetaData.getQueryName());
                totalCnt += recCount;
            }
            catch (Exception e)
            {
                new TvDaoException(e).logAndThrow(logger, "executeUpdateOnNamedQuery FAILED on ", query);
            }
        }
        return totalCnt;
    }

    private static boolean hasNotInClause(String queryString)
    {
        logger.debug("hasNotInClause() ", queryString.toUpperCase(Locale.ENGLISH));
        if (queryString.toUpperCase().contains(" NOT IN"))
        {
            return true;
        }
        return false;
    }

    /**
     * Create one or more Query objects with parameters applied. If any values that are a sizable Collection or Array,
     * create multiple queries where each query operates on a subset of the large list. (refactored from
     * applyNamedParametersToQuery) <br>
     * (WFM-5267: this caused "ORA-01795: maximum number of expressions in a list is 1000")
     * 
     * @param callerInfo - to identify calling method.
     * @param session
     * @param queryMetaData
     * @throws HibernateException
     * @throws TvDaoException
     * @return one or more Query objects
     */
    private static List<Query> createQueriesFromNamedParameters(String callerInfo, Session session,
            NamedQueryMetaData queryMetaData) throws HibernateException, TvDaoException
    {
        int maxParmValues = inexpression_max_value;

        String[] names = queryMetaData.getParamNames();
        Object[] values = queryMetaData.getParamValues();

        if (names.length != values.length)
        {
            logger.error(callerInfo, "() #parms=", names.length, ", #vals=", values.length, ": ", queryMetaData);
            throw new IllegalArgumentException("Number of values has to be the same as number of names");
        }
        logger.debug(callerInfo, "() #parms=", names.length, ": ", queryMetaData.getQueryName());
        logger.trace("   names:  ", names);
        logger.trace("   values: ", values);

        // If any parameter has a large list, we'll split it into smaller lists.
        Map<String, Collection<Object>> parmsWithLargeLists = findLargeListValues(callerInfo, queryMetaData,
            maxParmValues);

        String listparmName = "NONE";
        List<List<Object>> listParmChunks = null;
        if (parmsWithLargeLists.isEmpty())
        {
            // create a fake list so we do the listChunk loop once.
            listParmChunks = new ArrayList<List<Object>>(1);
            listParmChunks.add(null);
        }
        else if (parmsWithLargeLists.size() == 1)
        {
            Map.Entry<String, Collection<Object>> parmWithLargeList = parmsWithLargeLists.entrySet().iterator().next();
            listparmName = parmWithLargeList.getKey();
            // listParmChunks = getListChunks(parmWithLargeList.getValue(), maxParmValues );
            listParmChunks = Utils.splitListIntoLists((parmWithLargeList.getValue().size() / maxParmValues) + 1,
                Utils.collectionToList(parmWithLargeList.getValue()));
        }
        else
        {
            // can't handle more than one large in-list
            String msg = callerInfo + ": too many large list parameters: " + queryMetaData.getQueryName()
                    + " parmNames=" + parmsWithLargeLists.keySet();
            logger.error(msg);
            throw new TvDaoException(msg);
            // new TvDaoException(msg).logAndThrow(logger); // this way doesn't count in coverage stats
        }

        // create a list of queried, each with the largeListParm having one chunk of the list.
        // Must loop thru all parameters for each chunk b/c of how namedQueries are created.
        List<Query> queries = new ArrayList<Query>();

        Query query = null;
        for (List<Object> listParmChunk : listParmChunks)
        {
            query = session.getNamedQuery(queryMetaData.getQueryName());
            logger.debug("qstring: ", query.getQueryString());
            for (int i = 0; i < names.length; i++)
            {
                String name = names[i];
                Object value = values[i];
                if (name.equals(listparmName))
                {
                    // use the current chunk instead of the long list.
                    query.setParameterList(listparmName, listParmChunk);
                    // make sure there's no "NOT IN" clause- that could cause all rows to be deleted.
                    if (hasNotInClause(query.getQueryString()))
                    {
                        String msg = callerInfo + ": invalid NOT IN clause: " + queryMetaData.getQueryName();
                        logger.error(msg);
                        throw new TvDaoException(msg);
                    }
                }
                else if (value instanceof Collection)
                {
                    query.setParameterList(name, (Collection<?>) value);
                }
                else if (value instanceof Object[])
                {
                    query.setParameterList(name, (Object[]) value);
                }
                else
                {
                    // ordinary parm value, set it in baseQuery
                    query.setParameter(name, value);
                }
            }
            queries.add(query);
        }

        logger.info("   createQueriesFromNamedParameters returns ", queries.size(), " queries for ", query);
        return queries;
    }

    /**
     * Look through NamedQueryMetaData for any parameter that has a large collection. If it is larger than maxNumValues,
     * log unconditionally and return the parm name w/ values. WFM-5267: split up value lists
     * 
     * @param <T>
     * @param callerInfo
     * @param queryMetaData
     * @param maxNumValues
     * @return Map of paramName -> paramValues
     */
    @SuppressWarnings("unchecked")
    private static final <T> Map<String, Collection<T>> findLargeListValues(String callerInfo,
            NamedQueryMetaData queryMetaData, int maxNumValues)
    {
        // If any parameter has a large list, we'll keep it outside of baseQuery.
        Map<String, Collection<T>> parmsWithLargeLists = new HashMap<String, Collection<T>>();

        String[] names = queryMetaData.getParamNames();
        Object[] values = queryMetaData.getParamValues();

        for (int i = 0; i < names.length; i++)
        {
            String name = names[i];
            Object value = values[i];
            if (value instanceof Collection)
            {
                int numValues = ((Collection<?>) value).size();
                if (numValues > 20)
                {
                    logger.notice(callerInfo, ": parms[", i, "]=", name, //
                        ": Collection.size=", numValues, ", query=", queryMetaData.getQueryName());
                }
                if (numValues > maxNumValues)
                {
                    parmsWithLargeLists.put(name, (Collection<T>) value);
                }
            }
            else if (value instanceof Object[])
            {
                int numValues = ((Object[]) value).length;
                if (numValues > 20)
                {
                    logger.notice(callerInfo, ": parms[", i, "]=", name, //
                        ": Array.length=", numValues, ", query=", queryMetaData.getQueryName());
                }
                if (numValues > maxNumValues)
                {
                    // return as a collection
                    parmsWithLargeLists.put(name, Arrays.asList((T[]) value));
                }
            }
        }
        return parmsWithLargeLists;
    }

    /**
     * Generic wrapper for executing a named query that is expected to return 0 or 1 result.
     * 
     * @param <T>
     * @param namedQuery
     * @param template
     * @return
     * @throws TvDaoException
     */
    @SuppressWarnings("unchecked")
    public static final <T> T findUniqueResult(NamedQueryMetaData namedQuery, HibernateTemplate template)
            throws TvDaoException
    {
        if ((namedQuery == null) || (template == null))
        {
            logger.warn("Invalid parm(s); findUniqueResult(" + namedQuery + ", template=" + template + ")");
            return null;
        }

        T results = null;

        try
        {
            logger.debug("findUniqueResult namedQuery=", namedQuery);

            results = (T) DataAccessUtils.requiredUniqueResult(template.findByNamedQueryAndNamedParam(
                namedQuery.getQueryName(), namedQuery.getParamNames(), namedQuery.getParamValues()));
        }
        catch (EmptyResultDataAccessException emptyDae)
        {
            logger.info(emptyDae, "findUniqueResult(", namedQuery, ", template=", template, ")");
        }
        catch (IncorrectResultSizeDataAccessException incorrectDae)
        {
            logger.warn(incorrectDae, "findUniqueResult(", namedQuery, ", template=", template, ")");
        }
        catch (Exception ex)
        {
            logger.error(ex, "findUniqueResult(", namedQuery, ", template=", template, ")");
            throw new TvDaoException("findUniqueResult(" + namedQuery + ", template=" + template + ")", ex);
        }

        return results;
    }

    /**
     * Generic wrapper for executing a detached criteria query that is expected to return 0 or 1 result.
     * 
     * @param <T>
     * @param criteria
     * @param template
     * @return
     * @throws TvDaoException
     */
    public static final <T> T findUniqueResult(DetachedCriteria criteria, HibernateTemplate template)
            throws TvDaoException
    {
        if ((criteria == null) || (template == null))
        {
            logger.warn("Invalid parm(s), criteria=" + criteria + ", template=" + template);
            return null;
        }

        // filter out duplicates from results
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        T result = null;
        try
        {
            logger.debug("findUniqueResult criteria=", criteria);

            @SuppressWarnings("unchecked")
            T unique = (T) DataAccessUtils.requiredUniqueResult(template.findByCriteria(criteria));
            result = unique;
        }
        catch (EmptyResultDataAccessException emptyDae)
        {
            logger.info(emptyDae, "findUniqueResult(", criteria, ", template=", template, ")");
        }
        catch (IncorrectResultSizeDataAccessException incorrectDae)
        {
            logger.warn(incorrectDae, "findUniqueResult(", criteria, ", template=", template, ")");
        }
        catch (Exception ex)
        {
            logger.error(ex, "findUniqueResult(", criteria, ", template=", template, ")");
            throw new TvDaoException("findUniqueResult(" + criteria + ", template=" + template + ")", ex);
        }

        return result;
    }

    /**
     * Sets criteria to retrieve distinct DateRanges from a table/relationship using the given field names. Query
     * results will be a List of DateRanges in ascending order.
     * 
     * @param criteria
     * @param startDateField
     * @param endDateField
     */
    public static final void setDistinctDateRangeCriteria(DetachedCriteria criteria, String startDateField,
            String endDateField)
    {
        if ((criteria != null) && !Utils.isBlank(startDateField) && !Utils.isBlank(endDateField))
        {
            ProjectionList projections = Projections.projectionList();
            projections.add(Projections.property(startDateField), "start");
            projections.add(Projections.property(endDateField), "end");
            criteria.addOrder(Order.asc(startDateField));
            criteria.setProjection(Projections.distinct(projections));
            criteria.setResultTransformer(new AliasToBeanResultTransformer(DateRange.class));
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), criteria=" + criteria + ", startDateField=" + startDateField
                    + ", endDateField=" + endDateField);
        }
    }

    /**
     * Generic method for IdName retrieval.
     * 
     * @param criteria
     * @param template
     * @param idField
     * @param nameField
     * @param oidField
     * @param sortCriteria
     * @param filterCriteria
     * @return
     * @throws TvDaoException
     */
    public static final <I extends IdName> Collection<I> getIdNames(Class<I> idNameClass, DetachedCriteria criteria,
            HibernateTemplate template, String idField, String nameField, String oidField,
            SortCriteria<IdName.SortBy> sortCriteria, FilterCriteria<IdName.Filter> filterCriteria)
            throws TvDaoException
    {
        Collection<I> queryResults = Collections.emptyList();

        if (criteria != null)
        {
            // For each non-blank field specified, add a projection.
            ProjectionList projections = Projections.projectionList();

            // Any sorting? Sorting can only occur on field(s) that are to be projected
            Set<FieldCriteria.Key> sortByKeys = null;
            if (sortCriteria != null)
            {
                sortByKeys = sortCriteria.getKeySupplier().getFieldKeys();
            }
            else
            {
                sortByKeys = Collections.emptySet();
            }

            Map<FieldCriteria.Key, FieldCriteria> sortFilterCriterias = new LinkedHashMap<FieldCriteria.Key, FieldCriteria>();

            // Include id field?
            if (!Utils.isBlank(idField))
            {
                projections.add(Projections.property(idField), IdName.ID_KEY.getField());

                // Allow sort by id, if specified
                if (sortByKeys.contains(IdName.ID_KEY))
                {
                    FieldCriteria.addSort(sortFilterCriterias, IdName.ID_KEY.getField(), sortCriteria.getOrder());
                }
            }

            // Include name field?
            if (!Utils.isBlank(nameField))
            {
                projections.add(Projections.property(nameField), IdName.NAME_KEY.getField());

                // Allow sort by name, if specified
                if (sortByKeys.contains(IdName.NAME_KEY))
                {
                    FieldCriteria.addSort(sortFilterCriterias, IdName.NAME_KEY.getField(), sortCriteria.getOrder());
                }
            }

            // Include oid field?
            if (!Utils.isBlank(oidField))
            {
                projections.add(Projections.property(oidField), IdName.OID_KEY.getField());

                // Allow sort by oid, if specified
                if (sortByKeys.contains(IdName.OID_KEY))
                {
                    FieldCriteria.addSort(sortFilterCriterias, IdName.OID_KEY.getField(), sortCriteria.getOrder());
                }
            }

            // At least one field must be supplied
            if (projections.getLength() > 0)
            {
                // FIXME (TWL, 2006-08-03): Hibernate-generated sql fails under Postgres, so until this is resolved
                // filter manually.
                // // Include the filter criteria
                // if ((filterCriteria != null) && (filterCriteria.isFiltered()))
                // {
                // for (FieldCriteria.Key fieldKey : filterCriteria.getKeySupplier().getFieldKeys())
                // {
                // FieldCriteria.addFilter(sortFilterCriterias, fieldKey, filterCriteria.getMatchString());
                // }
                // }

                criteria.setProjection(projections);
                criteria.setResultTransformer(new AliasToBeanResultTransformer(idNameClass));

                addFieldOptions(sortFilterCriterias, criteria);

                queryResults = findByCriteria(criteria, template);

                // FIXME (TWL, 2006-08-03): Hibernate-generated sql fails under Postgres, so until this is resolved
                // filter manually.
                queryResults = IdName.filter(queryResults, filterCriteria);
            }
            else
            // All fields were blank
            {
                logger.warn("At least one field name must be non-blank, criteria=" + criteria);
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), criteria=" + criteria + ", idField=" + idField + ", nameField=" + nameField
                    + ", oidField=" + oidField);
        }

        return queryResults;
    }

    /**
     * Adds a criteria restriction using the array elements. If no elements are provided, no restriction will be added.
     * 
     * <pre>
     * Examples (assume field is &quot;amount&quot;):
     * restrictions         pseudo where clause
     * ========================================
     * [10]                 where amount = 10
     * [10, 20, 30]         where amount in (10, 20, 30]
     * </pre>
     * 
     * @param criteria
     * @param field
     * @param restrictions
     */
    public static final <T> void addArrayRestriction(DetachedCriteria criteria, String field, T... restrictions)
    {
        if (criteria != null)
        {
            Criterion criterionToAdd = getArrayRestriction(field, restrictions);
            if (criterionToAdd != null)
            {
                criteria.add(criterionToAdd);
            }
        }
        else
        {
            logger.warn("Invalid parm(s): criteria=", criteria, ", field=", field, ", restrictions=", restrictions);
        }
    }

    /**
     * Returns criteria restriction using the array elements. If no elements are provided, null is returned.
     * 
     * <pre>
     * Examples (assume field is &quot;amount&quot;):
     * restrictions         pseudo where clause
     * ========================================
     * [10]                 where amount = 10
     * [10, 20, 30]         where amount in (10, 20, 30]
     * </pre>
     * 
     * @param criteria
     * @param field
     * @param restrictions
     */
    public static final <T> Criterion getArrayRestriction(String field, T... restrictions)
    {
        Criterion criterion = null;

        if (!Utils.isBlank(field))
        {
            if (!Utils.isEmpty(restrictions))
            {
                if (restrictions.length > 20)
                {
                    logger.notice("getArrayRestriction(field=", field, //
                        ", #restrictions=", restrictions.length);
                }

                criterion = restrictions.length == 1 ? Restrictions.eq(field, restrictions[0])
                        : getAllCollectionRestrictions(field, Arrays.asList(restrictions));
            }
            else
            {
                logger.info("No restrictions supplied, field=", field);
            }
        }
        else
        {
            logger.warn("Invalid parm(s): field=", field, ", restrictions=", restrictions);
        }

        return criterion;
    }

    /**
     * Adds a criteria restriction using the collection elements. If no elements are provided, no restriction will be
     * added.
     * 
     * <pre>
     * Examples (assume field is &quot;amount&quot;):
     * restrictions         pseudo where clause
     * ========================================
     * [10]                 where amount = 10
     * [10, 20, 30]         where amount in (10, 20, 30]
     * </pre>
     * 
     * @param criteria
     * @param field
     * @param restrictions
     */
    public static final void addCollectionRestriction(DetachedCriteria criteria, String field,
            Collection<?> restrictions)
    {
        if (criteria != null)
        {
            Criterion criterionToAdd = getCollectionRestriction(field, restrictions);
            if (criterionToAdd != null)
            {
                criteria.add(criterionToAdd);
            }
        }
        else
        {
            logger.warn("Invalid parm(s): criteria=", criteria, ", field=", field, ", restrictions=", restrictions);
        }
    }

    /**
     * Returns a criteria restriction using the collection elements. If no elements are provided, null is returned.
     * 
     * <pre>
     * Examples (assume field is &quot;amount&quot;):
     * restrictions         pseudo where clause
     * ========================================
     * [10]                 where amount = 10
     * [10, 20, 30]         where amount in (10, 20, 30]
     * </pre>
     * 
     * @param field
     * @param restrictions
     */
    public static final Criterion getCollectionRestriction(String field, Collection<?> restrictions)
    {
        Criterion criterion = null;
        if (!Utils.isBlank(field))
        {
            if (!Utils.isEmpty(restrictions))
            {
                criterion = restrictions.size() == 1 ? Restrictions.eq(field, restrictions.iterator().next())
                        : getAllCollectionRestrictions(field, restrictions);
            }
            else
            {
                logger.info("No restrictions supplied, field=", field);
            }
        }
        else
        {
            logger.warn("Invalid parm(s): field=", field, ", restrictions=", restrictions);
        }
        return criterion;
    }

    /**
     * When there are too many restrictions on a single field (values for an IN clause), split then up, e.g.
     * "WHERE (field IN (x1,x2,...xN) OR field IN (y1,y2,...yN) )" TV4-7193 In Oracle, this caused
     * "ORA-01795: maximum number of expressions in a list is 1000" in a couple of views.
     * 
     * @param field
     * @param restrictions
     * @return addArrayRestriction(DetachedCriteria criteria, String field, T... restrictions)
     */
    private static Criterion getAllCollectionRestrictions(String field, Collection<?> restrictions)
    {
        // (entity.field in (?, ?, ?...) or entity.field in (?, ?, ?...) or entity.field in (?))

        Criterion criterion = null;

        if (restrictions.size() > 200)
        {
            logger.notice(" field ", field, ": No. of expressions in clause = ", restrictions.size(), " (called for ",
                restrictions.getClass().getCanonicalName(), ")");
        }

        ArrayList<?> listRest = Utils.collectionToArrayList(restrictions);
        int modVal = ((listRest.size() % inexpression_max_value) != 0 ? 1 : 0);
        int no_of_inexpressions = listRest.size() / inexpression_max_value + modVal;

        for (int counter = 0; counter < no_of_inexpressions; counter++)
        {
            int startIndex = counter * inexpression_max_value;

            int lastIndex = startIndex;
            if (counter == no_of_inexpressions - 1)
            {
                lastIndex = listRest.size();
            }
            else
            {
                lastIndex = startIndex + inexpression_max_value;
            }

            List<?> subList = listRest.subList(counter * inexpression_max_value, lastIndex);

            if (criterion == null)
            {
                criterion = Restrictions.in(field, subList);
            }
            else
            {
                criterion = Restrictions.or(criterion, Restrictions.in(field, subList));
            }
        }

        return criterion;
    }

    /**
     * Save's the specified persistable based on it's internal ChangeAction (ADD, UPDATE, or DELETE)
     * 
     * @param <P>
     * @param persistable
     * @param template
     * @param callerLogger
     * @return The saved persistable or null if the persistable was deleted.
     * @throws TvDaoException If a parameter validation error occurs or a db-related exception is thrown
     */
    public static final <P extends TvPersistable> P saveTvPersistable(P persistable, HibernateTemplate template,
            TvLogger callerLogger) throws TvDaoException
    {
        // Make sure we have a valid logger
        if (callerLogger == null)
        {
            callerLogger = logger;
        }

        // Verify input parm(s)
        if ((persistable == null) || (template == null))
        {
            callerLogger.error("Invalid parm(s): saveTvPersistable(", persistable, ",", template, ")");
            throw new TvDaoException("Invalid parm(s): saveTvPersistable(" + persistable + "," + template + ")");
        }

        try
        {
            switch (persistable.getChangeAction())
            {
            case ADD:
                template.save(persistable);
                break;

            case DELETE:
                template.delete(persistable);
                persistable = null;
                break;

            case UPDATE:
                template.update(persistable);
                break;

            default:
                callerLogger.warn("Invalid change action=", persistable.getChangeAction(), ", persistable=",
                    persistable);
                throw new TvDaoException("Invalid change action=" + persistable.getChangeAction() + ", persistable="
                        + persistable);
            }
        }
        catch (Exception except)
        {
            callerLogger.error(except, persistable.getChangeAction(), " failed, persistable=", persistable);
            throw new TvDaoException(persistable.getChangeAction() + " failed, persistable=" + persistable, except);
        }

        return persistable;
    }

    public static Criterion getDateRangeRestriction(String datePropertyName, DateRange dateRange)
    {
        Criterion criterion = null;

        if (!Utils.isBlank(datePropertyName))
        {
            // Ignore for null date ranges
            if (dateRange != null)
            {
                // Use most efficient criteria
                if (dateRange.getDayCount() == 1)
                {
                    criterion = Restrictions.eq(datePropertyName, dateRange.getStart());
                }
                else
                // Date range is more than one day
                {
                    if (dateRange.getStart() != null)
                    {
                        criterion = Restrictions.ge(datePropertyName, dateRange.getStart());
                    }

                    if (dateRange.getEnd() != null)
                    {
                        criterion = Restrictions.and(criterion, Restrictions.le(datePropertyName, dateRange.getEnd()));
                    }
                }
            }
        }
        else
        // Invalid input parm(s)
        {
            logger.warn("Invalid input(s), datePropertyName=" + datePropertyName + ", dateRange=" + dateRange);
        }

        return criterion;
    }

    /**
     * NOTICE: consider using a NamedQuery with executeUpdateOnNamedQuery(). createQueriesFromNamedParameters() now
     * splits large lists into multiple queries [WFM-5467] Builds HQL/native Queries whose "where" clause will include
     * an "in" conditional based on the provided parm values. Each query built will append this conditional to the
     * supplied sqlBuffer.
     * <p>
     * HQL looks like:
     * <p>
     * delete from FutureActivityInstance fai where fai.oid in (:oids) delete from FutureActivityInstance fai where
     * fai.oid in (:oids)
     * <p>
     * 
     * @param <T>
     * @param session Non-null Hibernate session that will be associated with the returned Queries.
     * @param sqlBuffer Non-null sql buffer that the "in" clauses will be appended to.
     * @param parmName The name of the parm (i.e. property) for the "in" clause.
     * @param maxParmValues The max number of parm values allowed in a single Query.
     * @param parmValues One or more parm values.
     * @return Returns 0 or more Queries.
     */
    public static final <T> List<Query> buildCollectionBasedQueries(Session session, StringBuilder sqlBuffer,
            String parmName, int maxParmValues, Collection<T> parmValues)
    {
        // Validate parms
        if ((session == null) || (sqlBuffer == null) || (parmName == null) || (maxParmValues <= 0)
                || Utils.isEmpty(parmValues))
        {
            logger.warn("Invalid parm(s); session=", session, ", sqlBuffer=", sqlBuffer, ", parmName=", parmName,
                ", maxParmValues=", maxParmValues);
            return Collections.emptyList();
        }

        List<Query> queries = new ArrayList<Query>();
        int oidsCount = parmValues.size();

        /**
         * We could add logic to use an "equals" instead of an "in" when the number of values == 1. However, most modern
         * query planners do this anyway, so ignore for now.
         */
        sqlBuffer.append(" in (:");
        sqlBuffer.append(parmName);
        sqlBuffer.append(")");

        String sql = sqlBuffer.toString();

        /**
         * If the number of values is already under the specified max, just add them as-is.
         */
        if (oidsCount <= maxParmValues)
        {
            queries.add(session.createQuery(sql).setParameterList("oids", parmValues));
        }
        else
        {
            /**
             * The number of values is larger than the specified max, so break up into chunks no larger than the max.
             * Each chunk will generate a new Query object.
             */
            int listCapacity = Math.min(maxParmValues, parmValues.size());
            List<T> parmValuesChunk = new ArrayList<T>(listCapacity);
            for (T parmValue : parmValues)
            {
                if (parmValuesChunk.size() == maxParmValues)
                {
                    queries.add(session.createQuery(sql).setParameterList("oids", parmValuesChunk));
                    parmValuesChunk = new ArrayList<T>(listCapacity);
                }

                parmValuesChunk.add(parmValue);
            }
            if (parmValues.size() > 20)
            {
                logger.notice("buildCollectionBasedQueries(", parmName, ", maxParmValues=", maxParmValues,
                    ", #values=", parmValues.size(), ")");
            }
            /**
             * If there are leftover values, create the final query
             */
            if (!parmValuesChunk.isEmpty())
            {
                queries.add(session.createQuery(sql).setParameterList("oids", parmValuesChunk));
            }
        }

        return queries;
    }

    /**
     * When there are too many restrictions on a single field (values for an IN clause), split then up, e.g.
     * "WHERE (field IN (x1,x2,...xN) OR field IN (y1,y2,...yN) )"
     * 
     * @param field
     * @param restrictions
     * @return the in clause restrictions string for a native query
     */
    public static String getAllCollectionRestrictions(String field, Set<String> restrictions)
    {
        // (entity.field in (?, ?, ?...) or entity.field in (?, ?, ?...) or entity.field in (?))
        StringBuilder strBuilder = null;

        if (!Utils.isEmpty(restrictions))
        {
            if (restrictions.size() > 200)
            {
                logger.notice(" field ", field, ": No. of expressions in clause = ", restrictions.size(),
                    " (called for ", restrictions.getClass().getCanonicalName(), ")");
            }

            ArrayList<String> listRest = Utils.collectionToArrayList(restrictions);
            int modVal = ((listRest.size() % inexpression_max_value) != 0 ? 1 : 0);
            int no_of_inexpressions = listRest.size() / inexpression_max_value + modVal;

            for (int counter = 0; counter < no_of_inexpressions; counter++)
            {
                int startIndex = counter * inexpression_max_value;

                int lastIndex = startIndex;
                if (counter == no_of_inexpressions - 1)
                {
                    lastIndex = listRest.size();
                }
                else
                {
                    lastIndex = startIndex + inexpression_max_value;
                }

                List<String> subList = listRest.subList(counter * inexpression_max_value, lastIndex);

                if (strBuilder == null)
                {
                    strBuilder = new StringBuilder(" (");
                    strBuilder.append(field);
                    strBuilder.append(" IN ( ");
                    int count = 1;
                    for (String accessorOid : subList)
                    {
                        strBuilder.append("'");
                        strBuilder.append(accessorOid);
                        strBuilder.append("'");
                        if (count != subList.size())
                        {
                            strBuilder.append(",");
                        }
                        count++;
                    }
                    strBuilder.append(") ");
                }
                else
                {
                    strBuilder.append(" OR ");
                    strBuilder.append(field);
                    strBuilder.append(" IN ( ");
                    int count = 1;
                    for (String accessorOid : subList)
                    {
                        strBuilder.append("'");
                        strBuilder.append(accessorOid);
                        strBuilder.append("'");
                        if (count != subList.size())
                        {
                            strBuilder.append(",");
                        }
                        count++;
                    }
                    strBuilder.append(") ");
                }
            }
            strBuilder.append(") ");
        }

        return (strBuilder != null) ? strBuilder.toString() : "";
    }

}
