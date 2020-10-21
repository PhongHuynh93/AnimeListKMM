//
//  DetailMangaVM.swift
//  iosApp
//
//  Created by Coder on 10/18/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

class DetailMangaVM: ObservableObject {
    @Published var list = [DetailMangaUI]()
    let detailMangaVM: DetailMangaViewModel
    
    init(vm: DetailMangaViewModel, manga: Manga) {
        // show image firstly
        var tempList = [DetailMangaUI]()
        tempList.append(DetailMangaUI(item: manga))
        self.list = tempList

        detailMangaVM = vm
        detailMangaVM.setManga(manga: manga)
        detailMangaVM.data.watch { list in
            tempList = [DetailMangaUI]()
            tempList.append(DetailMangaUI(item: manga))
            list!.forEach { item in
                switch (item) {
                case is DetailMangaMoreInfo:
                    tempList.append(DetailMangaUI(item: item))
                case is DetailMangaCharacter:
                    let detailMangaCharacter = (item as! DetailMangaCharacter)
                    let oldList = detailMangaCharacter.list
                    var characterList = [CharacterUI]()
                    oldList.forEach { character in
                        characterList.append(CharacterUI(item: character))
                    }
                    tempList.append(DetailMangaUI(item: CharacterListUI(title: detailMangaCharacter.title, list: characterList)))
                default:
                    break
                }
            }
            self.list = tempList
        }
    }
    
    deinit {
        detailMangaVM.onCleared()
    }
}
