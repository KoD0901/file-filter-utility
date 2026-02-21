# Утилита фильтрации содержимого файлов

## Описание
Программа для фильтрации содержимого текстовых файлов. Разделяет целые числа,
вещественные числа и строки по разным выходным файлам.

## Требования
- Java 11 или выше
- Maven 3.6+ (для сборки)

## Сборка проекта
````bash
mvn clean package
````
## Использование
````bash
java -jar target/util.jar [опции] файл1 файл2
````
## Примеры запуска
````bash
# Базовая фильтрация
java -jar target/util.jar in1.txt in2.txt

# С префиксом и путем
java -jar target/util.jar -o results -p test- in1.txt in2.txt

# Режим добавления с полной статистикой
java -jar target/util.jar -a -f -p sample- in1.txt in2.txt
````