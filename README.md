# auction_test
Сервис получения релевантных рекламных баннеров по заданным фильтрам и сортировкам.
Основные бизнес-требования для сервиса:
1) Результат должен включать в себя баннеры с максимальной стоимостью. 
2) Идентификатор рекламной кампании должен быть уникальным для баннеров в результате выдачи. 
3) Баннеры должны фильтроваться по определенной стране. Если у баннера список стран пуст, он доступен для любой страны. 
4) Если у баннеров одинаковая стоимость и нужно выбрать один из них, то вероятность выбора будет одинакова у каждого. 

Дополнительные требования:
1) Возможность использования дополнительных фильтров выдачи
2) Возможность приоритезации (сортировки) выдачи по дополнительным полям

В сервисе реализованы два метода. Один с учетом основных требований, другой с учетом дополнительных. Функционал можно объединить при необходимости. 
