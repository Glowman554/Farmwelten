CREATE TABLE IF NOT EXISTS `worldLevels` (
    `uuid` varchar(100) NOT NULL,
    `worldLevel` int NOT NULL,
    `worldId` varchar(100) NOT NULL,
    PRIMARY KEY (`uuid`,`worldId`)
);

CREATE TABLE IF NOT EXISTS `scheduledTeleports` (
    `uuid` varchar(100) NOT NULL,
    `worldLevel` int NOT NULL,
    `worldId` varchar(100) NOT NULL,
    PRIMARY KEY (`uuid`)
);