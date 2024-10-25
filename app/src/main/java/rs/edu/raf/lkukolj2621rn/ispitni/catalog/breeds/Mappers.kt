package rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds

import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.api.BreedApiData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.api.ImageApiData
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.db.Breed
import rs.edu.raf.lkukolj2621rn.ispitni.catalog.breeds.db.Image

fun BreedApiData.asBreed() = Breed(
    id = id,
    name = name,
    altNames = alt_names,
    description = description,
    temperament = temperament,
    adaptability = adaptability,
    affectionLevel = affection_level,
    childFriendly = child_friendly,
    dogFriendly = dog_friendly,
    energyLevel = energy_level,
    lifeSpan = life_span,
    origin = origin,
    rare = rare,
    weight = weight.metric,
    wikipediaUrl = wikipedia_url,)

fun Breed.asBreedData() = BreedData(
    id = id,
    name = name,
    alternateNames = altNames?.split(", ") ?: emptyList(),
    description = description,
    temperament = temperament.split(", "),
    adaptability = adaptability,
    affectionLevel = affectionLevel,
    childFriendly = childFriendly,
    dogFriendly = dogFriendly,
    energyLevel = energyLevel,
    lifespan = lifeSpan,
    origins = listOf(origin),
    rare = rare != 0,
    weight = weight,
    wikipediaLink = wikipediaUrl,
    gotAllImages = gotAllImages)

fun ImageApiData.asImage(breedId: String) = Image(
        id = id,
        url = url,
        breedId = breedId)

fun Image.asImageData() = ImageData(
        id = id,
        url = url,
        breedId = breedId)