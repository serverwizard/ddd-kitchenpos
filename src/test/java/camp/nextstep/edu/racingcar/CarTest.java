package camp.nextstep.edu.racingcar;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@DisplayName("자동차 객체에 대한 테스트")
class CarTest {

    @DisplayName("자동차 이름이 5글자를 넘으면 IllegalArgumentException 예외를 발생한다.")
    @Test
    void 자동차_이름이_5글자를_넘으면_IllegalArgumentException_예외를_발생한다() {
        // expect
        assertThatThrownBy(() -> new Car("서버위저드 차"))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("자동차 이름이 5글자 이하면 자동차를 정상적으로 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {"서", "서버위", "서버위저드"})
    void 자동차_이름이_5글자_이하면_자동차를_정상적으로_생성한다(String name) {
        // given
        Car car = new Car(name);
        // expect
        assertThat(car.matchByName(name)).isTrue();
    }

    @DisplayName("자동차의 위치가 4이상인 경우 자동차는 움직인다")
    @ParameterizedTest
    @ValueSource(ints = {4, 5, 6})
    void 자동차의_위치가_4이상인_경우_자동차는_움직인다(int position) {
        // given
        Car car = new Car("위저드", position);
        // when
        car.move(new RandomMovingStrategy(new FixedNumberGenerator(position)));
        // then
        assertThat(car.isInPosition(position)).isFalse();
    }

    @DisplayName("자동차의 위치가 4미만인 경우 자동차는 안움직인다")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void 자동차의_위치가_4미만인_경우_자동차는_안움직인다(int position) {
        // given
        Car car = new Car("위저드", position);
        // when
        car.move(new RandomMovingStrategy(new FixedNumberGenerator(position)));
        // then
        assertThat(car.isInPosition(position)).isTrue();
    }

    @DisplayName("자동차 이름이 null일 경우 IllegalArgumentException 예외를 발생한다")
    @ParameterizedTest
    @NullSource
    void 자동차_이름이_null_일경우_IllegalArgumentException_예외를_발생한다(final String name) {
        // expect
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Car(name));
    }

    @DisplayName("자동차 이름이 빈 문자열일 경우 IllegalArgumentException 예외를 발생한다")
    @Test
    void 자동차_이름이_빈_문자열_일경우_IllegalArgumentException_예외를_발생한다() {
        // expect
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Car(""));
    }

    @TestFactory
    @DisplayName("자동차가 적절한 길이의 이름을 가졌는지 테스트 한다")
    Collection<DynamicTest> 자동차가_적절한_길이의_이름을_가졌는지_테스트_한다() {
        return Arrays.asList(
                DynamicTest.dynamicTest("자동차 이름이 5글자 이하면 자동차를 정상적으로 생성한다.", () ->
                        Arrays.asList("서", "서버", "서버위", "서버위저", "서버위저드")
                                .forEach(name -> assertThatCode(() -> new Car(name)).doesNotThrowAnyException())
                ),
                DynamicTest.dynamicTest("자동차 이름이 5글자를 넘으면 IllegalArgumentException 예외를 발생한다.", () ->
                        Arrays.asList("서버위저드차", "서버위저드 차")
                                .forEach(name -> assertThatThrownBy(() -> new Car(name)).isExactlyInstanceOf(IllegalArgumentException.class))
                )
        );
    }

    @DisplayName("자동차의 위치가 4미만인 경우 자동차는 안움직인다")
    @ParameterizedTest
    @MethodSource("carPositionProviderWith4Under")
    void 자동차의_위치가_4미만_인경우_자동차는_안움직인다(int position) {
        // given
        Car car = new Car("위저드", position);

        // when
        car.move(new RandomMovingStrategy(new FixedNumberGenerator(position)));

        // then
        assertThat(car.isInPosition(position)).isTrue();
    }

    private static Stream<Arguments> carPositionProviderWith4Under() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3)
        );
    }

    @DisplayName("자동차의 위치가 4 이상 인경우 자동차는 움직인다")
    @ParameterizedTest
    @MethodSource("carPositionProviderWith4Over")
    void 자동차의_위치가_4이상_인경우_자동차는_움직인다(int position) {
        // given
        Car car = new Car("위저드", position);

        // when
        car.move(new RandomMovingStrategy(new FixedNumberGenerator(position)));

        // then
        assertThat(car.isInPosition(position)).isFalse();
    }

    private static Stream<Arguments> carPositionProviderWith4Over() {
        return Stream.of(
                Arguments.of(4),
                Arguments.of(5),
                Arguments.of(6),
                Arguments.of(7)
        );
    }
}
