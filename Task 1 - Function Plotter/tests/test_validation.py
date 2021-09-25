import pytest
from utils import SUCCESS, validate_input, ERRORS, default_fn, default_x_min, default_x_max

x_min_default = default_x_min
x_max_default = default_x_max

# Validation function tests
# Responsible for checking basic functionality of `validate_input`
# ================================================================


def test_defaults():
    error_codes, x_min, x_max = validate_input(
        default_fn, x_min_default, x_max_default)
    assert SUCCESS in error_codes and len(error_codes) == 1


def test_no_x():
    fn = "3*(-2) + 9"
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert ERRORS['no x'] in error_codes and len(error_codes) == 1


def test_consec_xs():
    fn = '3*xx+2-x^5'
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert ERRORS['consec'] in error_codes and len(error_codes) == 1


def test_consec_symbols():
    # Even though `**` valid for sympy and I actually replace `^` with `**`
    # I chose to make invalid in the valdation function
    # just to make the validation function clear and simple
    fn = "3**x+7"
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert ERRORS['consec'] in error_codes and len(error_codes) == 1


def test_consec_symbols2():
    fn = "3*x++7"
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert ERRORS['consec'] in error_codes and len(error_codes) == 1


def test_invalid_symbols1():
    fn = "3;x,2!"
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert ERRORS['invalid symbol'] in error_codes and len(error_codes) == 1


def test_invalid_symbols2():
    fn = "3*x+2*x+4!"
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert ERRORS['invalid symbol'] in error_codes and len(error_codes) == 1


def test_invalid_symbols3():
    fn = "3*x+[2*x]+4"
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert ERRORS['invalid symbol'] in error_codes and len(error_codes) == 1


def test_malformed1():
    fn = "3x+6"
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert ERRORS['malformed'] in error_codes and len(error_codes) == 1


def test_malformed2():
    fn = "x5+6"
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert ERRORS['malformed'] in error_codes and len(error_codes) == 1


def test_malformed3():
    fn = "x*5+6-"
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert ERRORS['malformed'] in error_codes and len(error_codes) == 1


def test_malformed3():
    fn = "x*5+6/"
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert ERRORS['malformed'] in error_codes and len(error_codes) == 1


def test_parse_min1():
    fn = '3*x+2'
    error_codes, x_min, x_max = validate_input(fn, '3.x', x_max_default)
    assert ERRORS['parse error'] in error_codes and len(error_codes) == 1


def test_parse_min2():
    fn = '3*x+2'
    error_codes, x_min, x_max = validate_input(fn, 'asd12312ml', x_max_default)
    assert ERRORS['parse error'] in error_codes and len(error_codes) == 1


def test_parse_max1():
    fn = '3*x+2'
    error_codes, x_min, x_max = validate_input(fn, x_min_default, '56,z')
    assert ERRORS['parse error'] in error_codes and len(error_codes) == 1


def test_parse_max2():
    fn = '3*x+2'
    error_codes, x_min, x_max = validate_input(fn, x_min_default, 'zxc,.das')
    assert ERRORS['parse error'] in error_codes and len(error_codes) == 1


def test_complicated_fn1():
    fn = '3*x + 1/2 + (x)^(1/4)'
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert SUCCESS in error_codes and len(error_codes) == 1


def test_complicated_fn2():
    fn = '3*x*(2+X) + 1/(x^(5+x)) + 1/((x-2)^3)'
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert SUCCESS in error_codes and len(error_codes) == 1


def test_weird1():
    fn = "x*5+6/(-2)"
    error_codes, x_min, x_max = validate_input(
        fn, x_min_default, x_max_default)
    assert SUCCESS in error_codes and len(error_codes) == 1
