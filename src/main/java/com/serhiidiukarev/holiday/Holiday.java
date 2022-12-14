package com.serhiidiukarev.holiday;

import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;


/**
 * The class Holiday
 */
@Entity
@Table
public class Holiday {
    @SerializedName("id")
    @Id
    @SequenceGenerator(
            name = "holiday_sequence",
            sequenceName = "holiday_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "holiday_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "id")
    private Long holidayId;
    @SerializedName("date")
    @Column(name = "date", nullable = false)
    private LocalDate holidayDate;
    @SerializedName("name")
    @Column(name = "name")
    private String holidayName;
    @SerializedName("category")
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private HolidayCategory holidayCategory;


    /**
     * It is a constructor.
     *
     * @param holidayId       the long
     * @param holidayDate     the local date
     * @param holidayName     the display name
     * @param holidayCategory the holiday category
     */
    private Holiday(Long holidayId,
                    LocalDate holidayDate,
                    String holidayName,
                    HolidayCategory holidayCategory) {
        this.holidayId = holidayId;
        this.holidayDate = holidayDate;
        this.holidayName = holidayName;
        this.holidayCategory = holidayCategory;
    }

    private Holiday(LocalDate holidayDate,
                    String holidayName,
                    HolidayCategory holidayCategory) {
        this.holidayDate = holidayDate;
        this.holidayName = holidayName;
        this.holidayCategory = holidayCategory;
    }

    public Holiday() {
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
    public Long getHolidayId() {
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
    public void setHolidayId(Long holidayId) {
        this.holidayId = holidayId;
    }


    /**
     * Sets the holiday date
     *
     * @param holidayDate the holiday
     */
    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }


    /**
     * Sets the holiday name
     *
     * @param holidayName the display name
     */
    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }


    /**
     * It is a constructor.
     *
     * @param holidayCategory the holiday category
     */
    public void setHolidayCategory(HolidayCategory holidayCategory) {
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
        private Long holidayId;
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
        public HolidayBuilder holidayId(Long holidayId) {
            this.holidayId = holidayId;
            return this;
        }


        /**
         * It is a constructor.
         *
         * @param holidayDate the date
         */
        public HolidayBuilder holidayDate(LocalDate holidayDate) {
            this.holidayDate = holidayDate;
            return this;
        }


        /**
         * It is a constructor.
         *
         * @param holidayName the display name
         */
        public HolidayBuilder holidayName(String holidayName) {
            this.holidayName = holidayName;
            return this;
        }


        /**
         * It is a constructor.
         *
         * @param holidayCategory the holiday category
         */
        public HolidayBuilder holidayCategory(HolidayCategory holidayCategory) {
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
