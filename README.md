# Quizes
Создание и прохождение опросов, получение статистики о прохождениях опросов.

 -----

Учебный проект. Создан осенью 2020 года. В поекте есть возможность создания и прохождения опросов, а также получение определённой статистики.

В программе опросы представляют собой список вопросов. Каждый вопрос представляет собой объект соответствующего класса с следующими параметрами: тип вопроса, текст вопроса, варианты ответов на вопрос. Тип вопроса - целое число, от которого зависит формат ответа на этот вопрос:
 0 - данный вопрос - надпись, не требующая никакого ответа; 
 1 - в качестве ответа на данный вопрос пользователь введёт номер одного, выбранного варианта ответа из предложенного списка возможных вариантов ответа; 
 2 - в качестве ответа на данный вопрос пользователь введёт номера нескольких выбранных вариантов ответа из предложенного списка возможных вариантов; 
 3 - нет готовых вариантов ответа, в качестве ответа на данный вопрос пользователь введёт поизвольный ответ. 
Текст вопроса это надпись, которая выводится пользователю перед вариантами ответов (если тип вопроса 0, то это текст самой надписи). Варианты ответа в классе представляет собой массив возможных варианто ответа. 

При создании опроса в программе для поля варианты ответа требуется ввести одну строку, в которой через точку с запятой и пробел("; ") вводятся возможные варианты ответа без нумерации. Также в создаваемом опросе обязательно должны быть следующие вопросы в указанном формате для возможности получения статистики:
1. вопрос "Какой у вас пол?" 1 типа
2. вопрос "Сколько вам лет?" 1 типа
3. вопрос, начинающийся с "Как бы вы оценили", 2 типа


Для любого опроса можно получить следующую статистику:
1) общая - количество ответивших определённым образом на вопрос
2) по полу - количество ответивших определённым образом на вопрос с разбиением по полу
3) по возрасту - количество ответивших определённым образом на вопрос с разбиением по возрасту
4) по полу и возрасту - количество ответивших определённым образом на вопрос с разбиениями по полу и возрасту
5) рейтинг - рейтинг опроса

