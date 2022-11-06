SELECT
    m.title,
    m.duration,
    r.rating,
    GROUP_CONCAT(g.genre,'/') genre_list,
    GROUP_CONCAT(g.genre_id) genre_id_list,
    mg.movie_id
FROM MovieGenres mg
JOIN Movies m ON mg.movie_id = m.movie_id
JOIN Genres g ON mg.genre_id = g.genre_id
JOIN Ratings r ON m.rating_id = r.rating_id
GROUP BY m.title