package com.serhiidiukarev.holiday.validation;

import com.serhiidiukarev.holiday.Holiday;
import com.serhiidiukarev.holiday.repository.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("ALL")
@ExtendWith(MockitoExtension.class)
class ValidationHelperTest {

    private ValidationHelper underTest;

    @Mock
    private static HolidayRepository holidayRepository;

    @BeforeEach
    void setUp() {
        underTest = new ValidationHelper(holidayRepository);
    }

    @Test
    void validateHoliday() {
        // given
        Holiday holiday = null;
        // when
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> underTest.validateHoliday(holiday));
        // then
        assertEquals(illegalArgumentException.getMessage(), "Invalid method argument: (holidays=null)");
    }

    @Test
    void validateDate() {
        // given
        final LocalDate date = null;
        // when
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> underTest.validateDate(date));
        // then
        assertEquals(illegalArgumentException.getMessage(), "Invalid method argument: (date=null)");
    }

    @Test
    void validateDates() {
        // case 1
        // given
        final LocalDate start1 = LocalDate.of(2022, 8, 19);
        final LocalDate end1 = null;
        // when
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> underTest.validateDates(start1, end1));
        // then
        assertEquals(illegalArgumentException.getMessage(), "Invalid method argument(s): (startDate=2022-08-19, endDate=null)");

        // case 2
        // given
        final LocalDate start2 = null;
        final LocalDate  end2 = LocalDate.of(2022, 8, 19);
        // when
        illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> underTest.validateDates(start2, end2));
        // then
        assertEquals(illegalArgumentException.getMessage(), "Invalid method argument(s): (startDate=null, endDate=2022-08-19)");
    }

    @Test
    void isHolidayAlreadyExisted() {
        // case 1
        // given
        final Holiday holiday1 = null;
        // when
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> underTest.isHolidayAlreadyExisted(holiday1));
        // then
        assertEquals(illegalArgumentException.getMessage(), "Invalid method argument: (holidays=null)");

        // case 2
        // given
        final LocalDate holidayDate = LocalDate.of(2022, 8, 19);
        final String holidayName = "Holiday Name";
        final Holiday.HolidayCategory holidayCategory = Holiday.HolidayCategory.CUSTOM;
        final Holiday holiday2 = Holiday
                .builder()
                .holidayDate(holidayDate)
                .holidayCategory(holidayCategory)
                .holidayName(holidayName)
                .build();

        // when
        underTest.isHolidayAlreadyExisted(holiday2);
        // then
        ArgumentCaptor<String> nameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<LocalDate> localDateArgumentCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<Holiday.HolidayCategory> holidayCategoryArgumentCaptor = ArgumentCaptor.forClass(Holiday.HolidayCategory.class);

        verify(holidayRepository).findHoliday(localDateArgumentCaptor.capture(), nameArgumentCaptor.capture(), holidayCategoryArgumentCaptor.capture());

        String nameArgumentCaptorValue = nameArgumentCaptor.getValue();
        assertThat(nameArgumentCaptorValue).isEqualTo(holidayName);

        LocalDate localDateArgumentCaptorValue = localDateArgumentCaptor.getValue();
        assertThat(holidayDate).isEqualTo(localDateArgumentCaptorValue);

        Holiday.HolidayCategory holidayCategoryArgumentCaptorValue = holidayCategoryArgumentCaptor.getValue();
        assertThat(holidayCategory).isEqualTo(holidayCategoryArgumentCaptorValue);

        // case 3
        // given
        when(holidayRepository.findHoliday(holidayDate, holidayName, holidayCategory)).thenReturn(Optional.of(holiday2));

        // when
        illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> underTest.isHolidayAlreadyExisted(holiday2));
        // then
        assertEquals(illegalArgumentException.getMessage(), "already added");
    }

    @Test
    void isHolidayExist() {
        // given
        Long holidayId = 1L;
        when(holidayRepository.existsById(holidayId)).thenReturn(false);

        // when
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        IllegalArgumentException illegalArgumentException =
                assertThrows(IllegalArgumentException.class, () -> underTest.isHolidayExist(holidayId));
        // then
        verify(holidayRepository).existsById(longArgumentCaptor.capture());
        assertEquals(holidayId, longArgumentCaptor.getValue());
        assertEquals(illegalArgumentException.getMessage(), "holiday with id=" + holidayId + " does not exists");
    }
}