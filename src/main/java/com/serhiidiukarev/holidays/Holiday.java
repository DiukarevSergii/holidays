package com.serhiidiukarev.holidays;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.Objects;


/**
 * The class Holiday
 */
public class Holiday {
    @SerializedName("id")
    private Integer holidayId;
    @SerializedName("date")
    private LocalDate holidayDate;
    @SerializedName("name")
    private String holidayName;
    @SerializedName("category")
    private HolidayCategory holidayCategory;


    /**
     * It is a constructor.
     *
     * @param holidayId       the integer
     * @param holidayDate     the local date
     * @param holidayName     the display name
     * @param holidayCategory the holiday category
     */
    private Holiday(final Integer holidayId, final LocalDate holidayDate, final String holidayName, final HolidayCategory holidayCategory) {
        this.holidayId = holidayId;
        this.holidayDate = holidayDate;
        this.holidayName = holidayName;
        this.holidayCategory = holidayCategory;
    }


    /**
     * It is a constructor.
     */
    public static HolidayBuilder builder() {
        return new HolidayBuilder();
    }

    /**
     * @param o the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see java.util.HashMap
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Holiday)) return false;
        Holiday holiday = (Holiday) o;
        return Objects.equals(holidayDate, holiday.holidayDate)
                && Objects.equals(holidayName, holiday.holidayName)
                && holidayCategory == holiday.holidayCategory;
    }


    /**
     * Hash code
     *
     * @return a hash code value for this object.
     * @see java.lang.Object#equals(java.lang.Object)
     * @see java.lang.System#identityHashCode
     */
    @Override
    public int hashCode() {

        return Objects.hash(holidayDate, holidayName, holidayCategory);
    }


    /**
     * Gets the holiday identifier
     *
     * @return the holiday identifier
     */
    public Integer getHolidayId() {
        return this.holidayId;
    }


    /**
     * Gets the holiday date
     *
     * @return the holiday date
     */
    public LocalDate getHolidayDate() {
        return this.holidayDate;
    }


    /**
     * Gets the holiday name
     *
     * @return the holiday name
     */
    public String getHolidayName() {
        return this.holidayName;
    }


    /**
     * It is a constructor.
     */
    public HolidayCategory getHolidayCategory() {
        return this.holidayCategory;
    }


    /**
     * Sets the holiday identifier
     *
     * @param holidayId the id
     */
    public void setHolidayId(final Integer holidayId) {
        this.holidayId = holidayId;
    }


    /**
     * Sets the holiday date
     *
     * @param holidayDate the holiday
     */
    public void setHolidayDate(final LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }


    /**
     * Sets the holiday name
     *
     * @param holidayName the display name
     */
    public void setHolidayName(final String holidayName) {
        this.holidayName = holidayName;
    }


    /**
     * It is a constructor.
     *
     * @param holidayCategory the holiday category
     */
    public void setHolidayCategory(final HolidayCategory holidayCategory) {
        this.holidayCategory = holidayCategory;
    }

    /**
     * Holiday Category
     */
    public enum HolidayCategory {
        GOVERNMENT,
        CUSTOM,
        OTHER
    }

    /**
     * Holiday Builder
     */
    public static class HolidayBuilder {
        private Integer holidayId;
        private LocalDate holidayDate;
        private String holidayName;
        private HolidayCategory holidayCategory;

        HolidayBuilder() {
        }

        /**
         * It is a constructor.
         *
         * @param holidayId the id
         */
        public HolidayBuilder holidayId(final Integer holidayId) {
            this.holidayId = holidayId;
            return this;
        }


        /**
         * It is a constructor.
         *
         * @param holidayDate the date
         */
        public HolidayBuilder holidayDate(final LocalDate holidayDate) {
            this.holidayDate = holidayDate;
            return this;
        }


        /**
         * It is a constructor.
         *
         * @param holidayName the display name
         */
        public HolidayBuilder holidayName(final String holidayName) {
            this.holidayName = holidayName;
            return this;
        }


        /**
         * It is a constructor.
         *
         * @param holidayCategory the holiday category
         */
        public HolidayBuilder holidayCategory(final HolidayCategory holidayCategory) {
            this.holidayCategory = holidayCategory;
            return this;
        }


        /**
         * It is a constructor.
         */
        public Holiday build() {
            return new Holiday(this.holidayId, this.holidayDate, this.holidayName, this.holidayCategory);
        }


        /**
         * To string
         *
         * @return String
         */
        public String toString() {
            return "Holiday.HolidayBuilder(holidayId=" + this.holidayId + ", holidayDate=" + this.holidayDate + ", " +
                    "holidayName=" + this.holidayName + ", holidayCategory=" + this.holidayCategory + ")";
        }
    }
}
