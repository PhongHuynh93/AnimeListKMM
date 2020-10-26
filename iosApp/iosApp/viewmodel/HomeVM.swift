//
//  HomeVM.swift
//  iosApp
//
//  Created by Coder on 10/14/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

class HomeVM: ObservableObject {
    @Published var homeList = [HomeUI]()
    let homeVM: HomeMangaViewModel
    
    init(homeVM: HomeMangaViewModel) {
        self.homeVM = homeVM
        homeVM.data.watch { list in
            var indexOuter = 0
            var tempHomeList = [HomeUI]()
            list!.forEach { it in
                switch it {
                case is MangaList:
                    // convert nsarray into array
                    var indexInner = 0
                    let oldMangaList = it as! MangaList
                    var mangaList = [MangaUI]()
            
                    oldMangaList.list.forEach { it in
                        mangaList.append(MangaUI(pos: indexInner, manga: it))
                        indexInner += 1
                    }
                    
                    tempHomeList.append(HomeUI(pos: indexOuter, home: MangaListUI(title: oldMangaList.title, mangaList: mangaList)))
                default:
                    tempHomeList.append(HomeUI(pos: indexOuter, home: it as! Home))
                }
                indexOuter += 1
               
            }
            self.homeList = tempHomeList
        }
    }
    
    deinit {
        homeVM.onCleared()
    }
    
    func onItemAppear(itemId: Int) {
        print("onItemAppear")
        if (homeList.last!.id == itemId) {
            print("loadmore")
            homeVM.loadMore()
        }
    }
}
