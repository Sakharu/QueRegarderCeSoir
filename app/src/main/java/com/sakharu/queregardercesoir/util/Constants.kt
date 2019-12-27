package com.sakharu.queregardercesoir.util

/*
    CATEGORIES
 */
const val NUMBER_OF_CATEGORIES=3

const val CATEGORY_TRENDING_ID=1L
const val CATEGORY_TOPRATED_ID=2L
const val CATEGORY_NOWPLAYING_ID=3L

/*
    USER LISTS
 */
const val NUMBER_OF_BASE_LIST=3

const val USER_LIST_FAVORITE_ID=1L
const val USER_LIST_TOWATCH_ID=2L
const val USER_LIST_WATCHED_ID=3L

/*
    ACTIONS & EXTRA
 */
const val ACTION_FAVORITE_GENRE_VALIDATE="ActionFavoriteGenreValidate"
const val EXTRA_MOVIE_ID="ExtraMovieID"
const val EXTRA_MOVIE_NAME="ExtraMovieName"
const val EXTRA_IMAGE_URL="ExtraImageURL"
const val EXTRA_TYPE_IMAGE="ExtraTypeImage"
const val TYPE_POSTER="TypePoster"
const val TYPE_BACKDROP="TypeBackdrop"
const val EXTRA_CATEGORY = "category"
const val EXTRA_SORTBY = "ExtraSortBy"
const val EXTRA_BEFORE_DURING_AFTER = "ExtraIsBefore"
const val EXTRA_YEAR= "ExtraYear"
const val EXTRA_AVERAGE_VOTE_MIN = "ExtraAverageVoteMin"
const val EXTRA_GENRES = "ExtraGenres"
const val EXTRA_CERTIFICATION = "ExtraCertification"

/*
  PARAMETRES DE TRI
 */
const val POPULARITYDESC = "popularity.desc"
const val RELEASEDATEDESC = "release_date.desc"
const val VOTEAVERAGEDESC = "vote_average.desc"

/*
   PARAMETRES
 */
const val NUMBER_MAX_SELECTED_GENRE=5
const val ERROR_CODE=-1

/*
    NOM DES PREFERENCES
 */
const val PREFERENCE_LAST_TIMESTAMP_HOME="PreferenceLastTimeStampHome"
const val PREFERENCE_LAST_TIMESTAMP_DISCOVER="PreferenceLastTimeStampDiscover"
const val PREFERENCE_IDS_FAVORITES_GENRES="PreferenceIdsFavoritesGenres"

/*
    ID DES RECHERCHES COURANTES
 */
const val ID_BEST_RECENT_MOVIES=0
const val ID_MY_FAMILY_FIRST=1
const val ID_SUCCES_OF_LAST_TEN_YEARS=2

/*
    CERTIFICATIONS
 */
const val CERTIFICATION_FRANCE_NO_AGE_REQUIRED="U"
const val CERTIFICATION_FRANCE="FR"