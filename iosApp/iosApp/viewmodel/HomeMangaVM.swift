//
//  HomeVM.swift
//  iosApp
//
//  Created by Coder on 10/14/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

class HomeMangaVM: ObservableObject {
    @Published var homeList = [HomeUI]()
    let homeVM: HomeMangaViewModel
    
    init(homeVM: HomeMangaViewModel) {
        self.homeVM = homeVM
        homeVM.data.watch { list in
            var tempHomeList = [HomeUI]()
            list!.forEach { it in
                switch it {
                case is MangaList:
                    // convert nsarray into array
                    let oldMangaList = it as! MangaList
                    var mangaList = [MangaUI]()
            
                    oldMangaList.list.forEach { it in
                        mangaList.append(MangaUI(manga: it))
                    }
                    
                    tempHomeList.append(HomeUI(home: MangaListUI(title: oldMangaList.title, mangaList: mangaList, loadMoreInfo: oldMangaList.loadMoreInfo, page: Int(oldMangaList.page))))
                default:
                    tempHomeList.append(HomeUI(home: it as! Home))
                }
               
            }
            self.homeList = tempHomeList
        }
    }
    
    deinit {
        homeVM.onCleared()
    }
    
    func onItemAppear(itemId: UUID) {
        print("onItemAppear")
        if (homeList.last!.id == itemId) {
            print("loadmore")
            homeVM.loadMore()
        }
    }
}
