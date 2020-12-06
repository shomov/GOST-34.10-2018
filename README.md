## ГОСТ-34.10-2018
Информационная технология. Криптографическая защита информации. Процессы формирования и проверки электронной цифровой подписи.

[![Build Status](https://travis-ci.com/shomov/GOST-34.10-2018.svg?branch=main)](https://travis-ci.com/shomov/GOST-34.10-2018)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/01f80db73e3e48669df39336093f7ef6)](https://www.codacy.com/gh/shomov/GOST-34.10-2018/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=shomov/GOST-34.10-2018&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/01f80db73e3e48669df39336093f7ef6)](https://www.codacy.com/gh/shomov/GOST-34.10-2018/dashboard?utm_source=github.com&utm_medium=referral&utm_content=shomov/GOST-34.10-2018&utm_campaign=Badge_Coverage)

http://protect.gost.ru/document1.aspx?control=31&baseC=6&page=0&month=1&year=2019&search=&id=232149

Данная программа является консольным приложением, реализующее алгоритмы хэширования (ГОСТ-34.11-2018), создания и верификации электронной цифровой подписи стандарта ГОСТ 34.10-2018.

Возможны следующие вариации входных данных:

`-p (файл с параметрами эллептической кривой) -m (файл сообщения) -s (файл с секретным ключом) [-o (выходной файл для сохранения ЭП)]`

`-p (файл с параметрами эллептической кривой) -m (файл сообщения) -v (файл с ключом расшифровки)`

В случае необходимости сгенерировать ключ расшифровки:

`-p (файл с параметрами эллептической кривой) -q (файл с секретным ключом) -o (выходной файл для сохранения ключа)`

Данная работа призвана быть наглядным материалом в деле освоения новейших государственных стандартов защиты информации, поэтому каждая функция максимально документируется и описывается.

Реализация алгоритмов произведена в рамках курсовой работы дисциплины "Алгоритмы и структуры данных". В ходе работы применены для учебных целей решения PVS-Studio, а также изучены библиотеки QuickCheck, EasyRandom. 

Михаил Шомов
mikle@shomov.spb.ru

## GOST-34.10-2018
Information technology. Cryptographic data security. Signature and verification processes of electronic digital Main.Main.Main.signature.

http://protect.gost.ru/document1.aspx?control=31&baseC=6&page=0&month=1&year=2019&search=&id=232149

This program is a console application that implements hashing algorithms (GOST-34.11-2018), creating and verifying an electronic digital signature of the GOST 34.10-2018 standard.

The following input data variations are possible:

`-p (file with elliptic curve parameters) - m (message file) -s (secret key file) [-o (output file for saving the item instance)]`

`-p (file with elliptic curve parameters) - m (message file) -v (file with decryption key)`

Generate a decryption key if necessary:

`-p (file with elliptic curve parameters) - q (file with secret key) - o (output file for saving the key)`

The algorithms were implemented as part of the course work of the discipline "Algorithms and data structures". During the work, PVS-Studio solutions were used for training purposes, as well as the QuickCheck and EasyRandom libraries were studied.

Mikhail Shomov
mikle@shomov.spb.ru
