def my_max(iterable, key=lambda x: x):
    max_x = max_key = None

    for x in iterable:
        if max_x is None or key(x) > max_x:
            max_x = key(x)
            max_key = x

    return max_key


if __name__ == '__main__':
    max_int = my_max([1, 3, 5, 7, 4, 6, 9, 2, 0])
    max_char = my_max("Suncana strana ulice")
    max_string = my_max([
        "Gle", "malu", "vocku", "poslije", "kise",
        "Puna", "je", "kapi", "pa", "ih", "njise"])

    dictionary = {'burek': 8, 'buhtla': 5}
    max_in_dictionary = my_max(dictionary, key=dictionary.get)

    personList = [("Dean","Trkulja"), ("Antonijo","Tomić"), ("Ivan","Majnarić")]
    lastByLexi = my_max(personList)

    print(max_int)
    print(max_char)
    print(max_string)
    print(max_in_dictionary)
    print(lastByLexi)